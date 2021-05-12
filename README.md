# Pineapple ChatBot
The Pineapple ChatBot is an Antificial Inteligence based on Java and the DeepLearning4J libary. The Platform, that the bot will connect to by default is Discord, others can be implemented with relative ease. The goal to start with was to create a bot, that could help with solving technical issues in windows. Now it can access a number of API's, that help it fetch knowladge from all over the Internet, that help it to awnser all kinds of questions from the most differing toppics.

## Setup
Pinapple is written in Java, using the Maven package manager. If you want to build the project, you will be required to install Java, Maven, and Git. You will also need to set up varrius API's , sp please take a look into the "API Keys" section bofore going on. Now thta you are finished with that, the first step is to clone the GitHub repository onto your computer. You can do this using the following command, or by downloading the project directly from GitHub.

    git clone https://github.com/JanSchermer/TechSupport.git
   
In the second step you need to download and install varrius Maven packages. To do this switch into the project folder and run the following command.

    mvn install

When your build succeeded, you can now move on to packaging the application into a java libary. To do this you will need to run the following command. Once you did the jar file will be created as "./target/Pineapple-[VERSION].jar".

    mvn package

To run the file with 2Gb of RAM, you just need to type the folloing command. The amount of RAM and the name of the jar file are freely adjustable of cause.

    java -Xmx2048m -jar Pineapple.jar


## API Keys
The bot uses three main API's, that require a key: Google's KGSearch, WolframAlpha, and OpenWeather. Furthermore a key for Discord is needed, if you don't implement another platform.  If you want to set up the bot without any modification, it is REQUIRED to provide all keys in a file called "keys.json" in the working directory, in order for the bot to function correctly.

    {
    	"discord":  "EXAMPLE",
    	"googleapis.com":  "EXAMPLE",
    	"openweathermap.org":  "EXAMPLE",
    	"wolframalpha.com":  "EXAMPLE"
    }

## License
**[GNU GENERAL PUBLIC LICENSE](https://en.wikipedia.org/wiki/GNU_General_Public_License)**