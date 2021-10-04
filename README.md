# add-custom-headers
Extensions written in Python and Java that add custom headers to every request 

Working code implementation of the Python version is based on: https://stackoverflow.com/questions/31684024/burp-extension-add-header-to-response/37736825#37736825

Working code implementation of the Java version is based on: https://forum.portswigger.net/thread/unable-to-edit-the-content-headers-e5ce4a72

**The Java version is compatible with both Burp Suite Professional and Burp Suite Enterprise**

# Java
 1. **`git clone https://github.com/UthmanPortSwigger/add-custom-headers.git`**
 2. **`cd add-custom-headers/CustomHeaders/Java`**
 3. Edit **BurpExtender.java** using vim or a text editor of your choice. Change **urlMatchList** to match your scope, **headersToAdd** to match the headers you want to add, and set **checkForDuplicates** to true or false. In the example under Releases (v1.0), the scope is https://portswigger-labs.net and the headers added are "Header1: value1" and "Header2: Value2"
 4. Run **`./gradlew fatJar`** 
 5. Load the **Add-Headers-Java.jar** file created at **build/libs** into Burp Suite Professional under **`Extender > Extensions > Add`** or Burp Suite Enterprise under **`Cog/Settings icon > Extensions > Custom extensions > Upload extension`**

**Note for Enterprise users:** You will need to be running [Burp Suite Enterprise 2021.8](https://portswigger.net/burp/releases/enterprise-edition-2021-8?requestededition=enterprise) or later. Please build this extension with the Java 11 JRE in your `Enterprise installation directory > jres > 11.0.10.9.1 > Contents > Home`.<br />
The build command in step 4 will then become: `./gradlew -Dorg.gradle.java.home=/path-to-your-enterprise-installation-directory/jres/11.0.10.9.1/Contents/Home fatJar`

**General Note:** If you see a `permission denied: ./gradlew` error, please run `chmod +x gradlew` before the build command
# Python
 1. **`git clone https://github.com/UthmanPortSwigger/add-custom-headers.git`**
 2. **`cd add-custom-headers/CustomHeaders/Python`**
 3. Edit **Add-Headers-Python.py**
 4. Load into Burp Suite Professional under **`Extender > Extensions > Add`**. Please ensure that you have Jython installed  

# Helpful resources: 

- https://portswigger.net/blog/burp-extensions-added-to-burp-suite-enterprise-edition
- https://portswigger.net/burp/documentation/desktop/tools/extender
- https://portswigger.net/burp/extender
- https://portswigger.net/burp/extender/writing-your-first-burp-suite-extension

# Contributors: 
:octopus:	[@HannahLaw-PortSwigger](https://github.com/HannahLaw-Portswigger) :octopus:	
