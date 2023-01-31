# Game Engine

This is a simple game engine in Java.

It was made to be the graphics library for my programming language Stack++.

I actually never made something like this before, so I used the following resources:
- https://www.youtube.com/@GamesWithGabe
- https://docs.gl/
- https://www.glfw.org/documentation.html
- https://stackoverflow.com/ ;)

## How to build

1. clone git repo (``git clone https://github.com/OliverSchlueter/GameEngine.git``)
2. cd into the repo (``cd GameEngine``)
3. to build, run ``gradlew shadowJar``
4. the jar file will be located in ``build/libs/GameEngine-[version].jar``
5. have fun with it


## Use as dependency in gradle project
1. run ``gradlew publishToMavenLocal``

2. in your project add the following:
````gradle
repositories {
    mavenLocal()
    ...
}

dependencies {
    implementation 'de.oliver:GameEngine:version'
    ...
}
````