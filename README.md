# CLEVIS---Command-LinE-Vector-graphIcs-Software
CLEVIS is a vector graphics application that allows users to create and manipulate four fundamental shapes: rectangles, lines, circles, and squares. The system provides both a graphical user interface (GUI) and a command-line interface (CLI) for creating and manipulating vector graphics in a Cartesian coordinate system.

# Key features:

Create and manipulate vector shapes with precise coordinates
Visualize graphics in real-time through an interactive canvas
Support for undo/redo operations
Two interface options: GUI for visual interaction and CLI for command-based control
Shape detection at specific coordinates

# Getting Started
## Installation
Decompress the ZIP file into a folder
Navigate to ProjectCode/The IntelliJ IDEA project/
Double-click the run.bat file
After launching, you'll see:

A GUI window titled "Clevis – Graphical Editor"
An attached command prompt window showing execution information
Note: Java 8+ is required to run this application. If you encounter issues, ensure Java is properly installed and added to your PATH.

## Using CLEVIS
GUI Mode
The GUI interface consists of three main sections:

Graphics Display - Shows all created shapes

Drag to pan the canvas
Ctrl + Mouse Wheel to zoom in/out
"+" and "-" buttons for fixed-scale zooming
"Reset" button to restore original view
"Clear" button to remove all shapes (cannot be undone)
"UseCMD" button to switch to CLI mode
Command History - Records all executed commands

Clevis Terminal - Input area for commands (identified by green $ symbol)

CLI Mode
To access CLI mode:

Click the "UseCMD" button in the GUI
The command prompt window will change to accept commands
In CLI mode:

Commands are entered after the clevis> prompt
Execution results are displayed immediately
Error messages provide guidance for correction
Command Reference
1. Creating Shapes
Rectangle
text
rectangle <name> <x> <y> <width> <height>
Creates a rectangle with top-left corner at (x,y) and specified dimensions.

Example:

text
rectangle myRect -100 50 200 100
Line
text
line <name> <x1> <y1> <x2> <y2>
Creates a line segment between two points.

Example:

text
line myLine -100 -100 100 100
Circle
text
circle <name> <x> <y> <radius>
Creates a circle with center at (x,y) and specified radius.

Example:

text
circle myCircle 0 0 100
Square
text
square <name> <x> <y> <side>
Creates a square with top-left corner at (x,y) and specified side length.

Example:

text
square mySquare -100 100 200
2. Manipulating Shapes
Move
text
move <shapeName> <dx> <dy>
Moves a shape horizontally by dx and vertically by dy.

Example:

text
move myRect 5 5
Shape at Point
text
shapeat <x> <y>
Finds the topmost shape covering point (x,y). A shape covers a point if the minimum distance from the point to the shape's outline is < 0.05 (simulating a line width of 0.1).

Example:

text
shapeat 10.5 20.3
Returns the name of the topmost covering shape
Returns "Not found" if no shape covers the point
3. History Management
Undo
text
undo
Reverts the last successfully executed command.

Example:

text
undo
Redo
text
redo
Re-applies the last command that was undone.

Example:

text
redo
4. System Commands
Help
text
help
Displays all valid command syntax.

Example:

text
help
Quit
text
quit
Terminates the application.

Example:

text
quit
Troubleshooting
Common Issues
Issue	Solution
Unknown command!	Check spelling and use help to see valid commands
Command error! Please check 'help' for correct commands	Verify the number of parameters matches the command requirements
Command error in parameters!	Ensure numeric parameters are valid numbers and names don't conflict with existing shapes
Parameter Rules
All numeric parameters must be valid numbers
Width/height/radius/side must be greater than zero
Shape names must be unique
Coordinates (x, y) have no restrictions
