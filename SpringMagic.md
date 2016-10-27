#Spring-Magic

##Service & Controller
@Service annotation is used in your service layer and annotates classes that perform service tasks, often you don't use it but in many case you use this annotation to represent a best practice. For example, you could directly call a DAO class to persist an object to your database but this is horrible. It is pretty good to call a service class that calls a DAO. This is a good thing to perform the separation of concerns pattern.

@Controller annotation is an annotation used in Spring MVC framework (the component of Spring Framework used to implement Web Application). The @Controller annotation indicates that a particular class serves the role of a controller. The @Controller annotation acts as a stereotype for the annotated class, indicating its role. The dispatcher scans such annotated classes for mapped methods and detects @RequestMapping annotations.

[stackoverflow](http://stackoverflow.com/questions/15922991/is-spring-annotation-controller-same-as-service)