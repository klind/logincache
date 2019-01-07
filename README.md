# Login Cache

# Challenge
Using Java (preferred) or C# write a simple cache library. Then use that library to implement the following interface (in Java, feel free to convert to C#).
```java
public interface LoginService {
/**
 * Checks whether the user has logged in within the last 24 hours
 * 	does NOT imply a login
 * @param userId
 * @return
 *	True if the user HAS logged in within 24 hours
 *	False otherwise
*/
public boolean hasUserLoggedInWithin24(String userId);
	
/**
* Sets the last login time for the user to now.
* @param userId
*/
public void userJustLoggedIn(String userId);
}
```

To simplify things, you can use the following library to pretend read and write to the database.

```java
public class FakeDBAccess {
	public Date getLastLoginForUser(String userId) {
		if(Math.random() < .5) return new Date(System.currentTimeMillis()); 
			return new Date(System.currentTimeMillis()-42*60*60*1000);
		}
	}
	
	public void setLastLoginForUser(String userId, Date date) throws DBEx {
		// do nothing
	}
}
```
Important details to consider:
1. There are more users than can fit in the cache.
2. There are far more calls to the service than users in the database.
3. There are other programs that update the database other than this one.
3a. This means the cache can go stale!
3b. When does staleness matter?
3c. You have NO control over the other programs.

Time and submission:
You have 24 hours to complete this task. Please submit either .cs or .java files zipped. Also include a readme explaining the program, how to compile, how to run, the decisions you made, and answers to the questions below.

Questions:
1. What was the reasoning behind your implementation of the cache?

2. How does your cache improve performance?

3. What are the various usage patterns that make the cache more or less effective in terms of performance?
Discuss why the cache will be more effective under some scenarios and less effective under others. For example if hasUserLoggedInWithin24 is called 80 times in a row for the same person, the cache is very effective. When is it less effective?

If you have questions please feel free to ask them.


# Solution
### Setup
The application is develop with Spring Boot.   
When building and compiling make sure there is access to maven repository.   
To run the application execute the class
**com.code.logincache.LoginCacheApplication**  
This will start up the application in an embedded Tomcat server.   
Make sure port 8080 is not used when running the application. 

As I had good time, I implemented some REST endpoints and Swagger.  
Here you can test the application.  
**Swagger URL : http://localhost:8080/swagger-ui.html**   

#### Checkstyle:
I also added checkstyle using the default configuration, with a few suppression's
that can bee seen in checkstyle-suppressions.xml  
Run "mvn clean verify site", open the file /target/site/checkstyle.html to see
the result. 

#### UnitTest:
Execute the class   
**com.code.logincache.LoginCacheApplicationTest**


### Implementation
I created my own implementation of the cache  
**com.code.logincache.LoginCache**  
I am using a HashMap for the cache, and a LinkedList to keep track on what 
users are the oldest added to the cache.   
This gives a convenient way of removing the oldest object from the cache
when the cache needs to be reduced.  
If we did not need to keep track on what users where the 'oldest' in the cache, 
and could just reduce the cache when it was reaching a certain limit, an 
implementation using the ConcurrentHashMap from the JDK would have been sufficient. 

### Keeping the cache stale.  
To keep the cache stale (current) with the database, I implemented the logic 
so that if a user is not found in the cache, the database will be queried, and 
if the user is found, the user will be added to the cache. 
As it is stated that there are far more calls to the service than 
users in the database, and nothing is stated about users being removed from
the database, I did not implement logic for keeping the cache updated 
when users potentially are removed from the database. 
If this was needed, I would have implemented a scheduled task that would 
e.g. clear the cache every hour, or we would implements a schedule job that will 
remove all id's from the cache that no longer are in the database.      
 
### Why using a cache?
We want to limit the number of database calls as they are heavy and 
time consuming. This results in better performance, less I/O calls, 
better user experience. 
A database might even be sitting in another city, or even country, which would 
make it really heavy to query ever time we need a check. 

### Cache performance.  
As mentioned above the performance is significantly better when using an in
memory cache.  
The cache will especially give better performance with many calls to the service. 
If the cache size is set to 100, and there are thousands of calls to the service, but
for the same 50 users, the cache will perform super. All 50 user will be in the cache,
and no database I/0 needed. 
If the cache size is set to 10, and there are thousands of calls to the service, all
for different users (thousands of different id's), the cache will perform poorly, as
every time a user is not found in the cache, the database is called, and updating of 
the cache is performed. 
How big the cache should be depends on how many calls are made and how many different 
users do wo have. 



 





