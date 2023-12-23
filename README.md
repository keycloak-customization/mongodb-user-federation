### About:
It's about a Keycloak SPI for Mongodb users federation. With this SPI you can plug-in your mongodb users into Keycloak to be used in the auth flow.

### Deployment:
Since we're using a third party library in this module, then besides generating the jar file we'll need to declare the used libraries as modules in keycloak modules folder.
1. Go to **keycloak-installation-folder/modules/system/layers/keycloak/org**
2. create a root folder under "**mongodb**" and inside it another one "**main**"
3. Download mongodb java driver jar file and put it in the folder you created before (in the main folder)
4. Create a module descriptor file "**module.xml**" and insert the code below:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<module name="org.mongodb" xmlns="urn:jboss:module:1.3">
    <properties>
        <property name="jboss.api" value="private"/>
    </properties>

    <resources>
        <resource-root path="mongo-java-driver-3.12.14.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="org.apache.log4j"/>
    </dependencies>
</module>

```

5. Now, generate your jar file and put it in the keycloak deployement folder "keycloak/standalone/deployments", if everything goes right it should generate a file with the extension .deployed
6. Then go to Keycloak dashboard to enable your provider, go to "User Federation" from the menu on the left as in the picture as below

![Alt text](doc/image.png)

And select your provider from the providers dropdown

![Alt text](doc/Screenshot%202023-12-23%20at%2022.51.31.png)

It will redirect you to the provder settings, leave it all default and click "save"

![Alt text](doc/image-1.png)

Now as you confirmed, you can go and test, I've a dummy mongoDb database with a dummy user data

![Alt text](doc/image-2.png)

Let's test that with Postman

![Alt text](doc/image-3.png)

### Contribution:
All contributions are most welcome, feel free to clone this repo and add your PR.