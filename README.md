# SpinFetch

Spinning Donuts in your terminal!

## Demo

[torus demo](https://github.com/user-attachments/assets/379de8e0-26c8-4aed-b648-02c906d97f36)

[cube demo](https://github.com/user-attachments/assets/ed507b11-0042-486f-8376-c4216e18332b)

## Commands

|Command|Action|
|-|-|
|spinFetch torus|Renders a rotating torus with System Info|
|spinFetch cube|Renders a rotating cube with System Info|
|spinFetch info|Prints just the System Info|

## Install

> Make sure to have [java](https://www.oracle.com/in/java/technologies/downloads/) installed (The latest version since the system info api used doesn't exist in earlier ones).

```bash
./build.sh

java -jar spinFetch.jar <COMMAND>
```

Or build form scratch
```bash
javac -d build -sourcepath src src/cli/Main.java

jar --create --file spinFetch.jar --main-class cli.Main -C build .

java -jar spinFetch.jar <COMMAND>
```

## Explanation

### Components

1. `cli.Layout.java`
   
    Combines the output from SpinTorus and SpinCube and SystemInfo to create the final O/P to print to console.
    It pads the info area so that previous O/P cannot interfere in the current O/P.

2. `cli.SysemInfo.java`

    Retrives System Information like:
    - UserName @ SystemName
    - OS
    - Kernel
    - Uptime
    - CPU Information
    - Memory
    - CPU Load
  
    It uses API provided from Java for cross platform parameters like OS, Kernel, RAM Usage CPU.
    Other parameters are different for different OSes.

3. `commands.SpinCube.java`

   Creates the Frame to be rendered and passes it to Layout to be printed.

4. `commands.SpinTorus.java`

   Creates the Frame to be rendered and passes it to Layout to be printed.

5. `engine.Rotation.java`

   Defines methods for Rotation in X, Y, Z axes.

6. `engine.Vector.java`

   Defines the Vector class and methods like dot product and normalization function.

## Interesting Discussions

1. Depth of char issue
   
    The main object to be rendered is stored in `char[][] screen`. 

    The problem arises when the Donut being 3D, multiple points that can be printed to the console (front facing and backward character).
    It is solved using another 2D array `char[][] zbuffer` that tracks the depth.

    The point coordinates are converted to screen coordinates using this formula.
    ```java
    screenX = K1 * x / (z + K2)
    screenY = K1 * y / (z + K2)
    ```
    where `1/(z + K2)` is the invZ or `1/depth`, where `z + K2` is depth.
    Now we track which point has the lowest depth (closest to camera) or highest invZ (same), then update it in the screen and the zbuffer.

2. Surface Point calculation

    To define a point on a Donut, we can use R, r, theta, phi.

    <img width="511" height="433" alt="image" src="https://github.com/user-attachments/assets/9200461f-fb32-43b2-a57c-95b881bb4554" />


    where,
    > R -> Distance from center of Torus to center of pipe.
    > 
    > r -> Radius of Tube.
    > 
    > theta -> Azimuthal angle (angle measured from center of torus)
    > 
    > phi -> Poloidal angle (angle measured from center of the pipe)
   
    The formulae used are as follows:
    ```math
    x(\theta,\phi) = (R + r\cos\theta)\cos\phi
    ```

    ```math
    y(\theta,\phi) = (R + r\cos\theta)\sin\phi
    ```

    ```math
    z(\theta,\phi) = r\sin\theta
    ```
    
    Normal from a point on Torus

    ```math
    n_x = \cos\theta \cos\phi
    ```

    ```math
    n_y = \cos\theta \sin\phi
    ```

    ```math
    n_z = \sin\theta
    ```

    The normals are also calculated to get the dot product between the LIGHT vector and this normal.
   
    The product gives us the brightness value which we can convert to the character by choosing the corresponding char in the RAMP.
    Darker points have smaller space taking chars like full stop, Brighter points have chars like $ that take more space.

    For the Cube, each point can be defined by just three parameters, X, Y, Z.

    The normals are fixed for a face. There are 6 faces, thus six normals. The faces are rendered separately.

4. Vector Rotation

   A vector can be rotated or transformed by matrix multiplication of the vector and the rotation matrix.

   The formula for rotation on each axis can be boiled down to:

   Rotation about X axis -

   ```math
   x' = x
   ```

   ```math
   y' = y\cos A - z\sin A
   ```

   ```math
   z' = y\sin A + z\cos A
   ```

   Rotation about Y axis -

   ```math
   x' = x\cos A + z\sin A
   ```

   ```math
   y' = y
   ```

   ```math
   z' = -x\sin A + z\cos A
   ```

   Rotation about z axis -

   ```math
   x' = x\cos A - y\sin A
   ```

   ```math
   y' = x\sin A + y\cos A
   ```

   ```math
   z' = z
   ```
## References

1. inspired by Joma on [youtube](https://www.youtube.com/watch?v=sW9npZVpiMI) 
2. original [blog post](https://www.a1k0n.net/2011/07/20/donut-math.html) by Andy Sloane

The project is more verbose and easy to read than the impressive original compressed program by Andy Sloane.
