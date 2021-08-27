# add-custom-headers
Extensions written in Python and Java that add custom headers to every request 

Working code implementation of the Python version is based on: https://stackoverflow.com/questions/31684024/burp-extension-add-header-to-response/37736825#37736825

Working code implementation of the Java version is based on: https://forum.portswigger.net/thread/unable-to-edit-the-content-headers-e5ce4a72

**The Java version is compatible with both Burp Suite Professional and Burp Suite Enterprise**

# Java
 1. **`git clone https://github.com/UthmanPortSwigger/add-custom-headers.git`**
 2. **`cd add-custom-headers/CustomHeaders/Java`**
 3. Edit **BurpExtender.java** (using vim or a text editor of your choice) to reference the headers you want to add. By default, this is 'Header1: value1' and 'Header2: value2'
 4. Run **`./gradlew fatJar`** 
 5. Load the JAR file (**build/libs/Add-Headers-Java.jar**) into Burp Suite Professional under **`Extender > Extensions > Add`** or Burp Suite Enterprise (**`Cog/Settings icon > Extensions > Custom extensions > Upload extension`**)

# Python
 1. **`git clone https://github.com/UthmanPortSwigger/add-custom-headers.git`**
 2. **`cd add-custom-headers/CustomHeaders/Python`**
 3. Edit **Add-Headers-Python.py**
 4. Load into Burp Suite Professional (**`Extender > Extensions > Add`**). Please ensure that you have Jython installed  
