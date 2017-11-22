#### [HIRE US](http://vrgsoft.net/)

# FlipView
![alt text](https://github.com/VRGsoftUA/FlipView/blob/master/ezgif.com-video-to-gif-2.gif "gif")

# Usage

*For a working implementation, Have a look at the Sample Project - sample*

1. Include the library as local library project.
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.VRGsoftUA:FlipView:1.0.1'
}
```
2. You need to implement two generic interfaces:
Pair and Binder. Interface Pair give view information about models for left and right parts of view. Binder - is a kind of Adapter which bind model to front view

# Customization
| Method  | Description |
| ------------- | ------------- |
| setCameraDistance(int cameraDistance)  | If this value equal to 0, rotation 3D effect will be completely shown  |
| setCurrentPosition(int position)   | Sets current position in data set provided to this view  |
| setCyclic(boolean cyclic)   | Sets whether this view should show data set cyclically or not  |
| addBackInAnimListener(Animator.AnimatorListener listener)  | Adds a listener to back in animator (when back side starts to show itself) |
| addFrontInAnimListener(Animator.AnimatorListener listener) | Adds a listener to front in animator (when front side starts to show itself) |
| setHardwareAccelerate(boolean hardwareAccelerate) | Sets whether this view should use hardware acceleration or not. |
#### Contributing
* Contributions are always welcome
* If you want a feature and can code, feel free to fork and add the change yourself and make a pull request
