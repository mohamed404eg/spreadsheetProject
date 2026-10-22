# Textual 
<p>TextualSave , TextualRender </p>
I diced to make to sperated interface one for render another for save the model 
so late we can add more way for render and save methods so take befite of 
this spiration so if clent need to implement just save its okay and more flaxablty.


# The supported formula:
`SExp` it could be `SExp` or integer direct
## formula supported:
<p>SExp it could be SExp or integer direct</p>
   <ul>
     <LI>(SUM sexp...)</LI>
     <li>(SUB sexp sexp)</li>
     <li>(PRODUCT sexp...)</li>
     <li>(SQRT Sexp)</li>
     <li>(< Sexp Sexp) returns whether the first is less than the second.</li>
     <li>(AVG  Sexp...) take the average </li>
     <li>for A1:A2 this mean the same column just(A1-> A2), B2:C5 this mean all column from B2=38 to C5=67 w/ all there cell</li>
     <li>(i.e. C42:D53 A1:A4) reference it should be to right or below</li>
     <li>a Reference like A1</li>
   </ul>
<h2>Notes:</h2>
 <P> if Formula takes n args then gives it group of n args it will works normal as gives it args been expanded. </P>
 <p>exclude from any grouping: `<` </p>

# Update

### `SString`
- add `getValue()`
<p> display the String value w/o escape \ or " rather than rely on toString() so we can use it 
for GUI</p>

### `Worksheet: void set(Coord coord, String content);`
update set function to throw error if the content is Invalid Symbol Syntax (like A, B, 1 , 2) its 
not valid `Coord`.

### `DrawSpreadSheet`
changed to get its   headerH; headerW;cellHeight;cellWidth; form who init the construction so
its easy on the view to determent what the value ease to make easy on operations like `MouseCoordToCellCoord`
more easy.

# Run
You can run the program under jarName.jar
its take different args first 
`-in some-filename -eval some-cell`
will eval the cell at int this file or `-in some-filename -save some-new-filename` will save the file to new file 
"no comment in the first file will be copy just cell"

### View file w gui
`-in some-filename -gui `
will shwo the file w/ the gui 
### open empty gui
`-gui`
### #open editor view 
`-edit`
`java -jar hw_09_assignment.jar -in first50-trag-num.gOOD -edit`
### empty edit:
`java -jar JarName.jar -edit`
### multEdit
`java -jar JarName.jar -in wb2.gOOD -multEdit` AND `java -jar JarName.jar -multEdit`
### evaluate one cell workbook
`java -jar JarName.jar -in wb.gOOD -eval-wb A!A1`
### how?
<p> all you have to do open the cmd run the chosen command line </p>
`java -jar hw_09_assignment.jar -in input-file.txt -gui` 



# view design
the view implement `IView`.
in the view will appear scroll bar only if the content not fit the frame
the content cell will be clip if its wider than the `cellWidth` that i decide to be 60
<p>for the Height the cell content at the middle of the cellHeight ``cellHeight / 2``</p>
<p>the header have different backGround</p>
<p> there some Padding to adjust the text so not be so closer to the line not important to mention</p>
<p>I used DrawSpreadSheet custom JPanel to draw everything </p>
<p>Used JScrollPane instead of use custom JPanel</p>


## Changes from Assignment 6:

- Added EditorView as a new composite view. It contains the existing
  DrawSpreadSheet component and adds the editing toolbar.

- Added SelectionPanel/SelectionLayer to support selecting individual
  cells without modifying the existing spreadsheet drawing behavior.

- Changed the constructor of DrawSpreadSheet to accept the headerH, headerW, cellHeight,
  cellWidth, because the new selection component needs to
  use the same component for coordinate system.
- fix some issue at `visitSymbol` so now its return null if the symbol empty or doesn't exist.
- fix resize `WorksheetModel` so its double the sheet column and row if the set reaches the max row or column.
- update `DrawSpreadSheet` to resize also the maxY and maxX before every paintComponent.
- ADD `IViewModel` so the View can take the Controller and the model.
- DELETED `IView::addFeatures` no need for it anymore.
- ADD `ControllerTest` for test the controller and `FakeIView` for helping the test.
- ADD `IModelAdaptorSpreadSheet` to make the old view and the new one use the sme `DrawSpreadSheet` help us have more fixable.
- ADD `IController` to help us implement more fixable controller use in place take `IViewModel, IModelAdaptorSpreadSheet`.
- ADD `IView:IView` and move the mouse event outside the view to the Controller by ADD `MouseConfig`.
- ADD `ScrollChangeListener` for the listen to the scroll, `IMouseController:scrollChanged()` for tell the controller the scroll change.

## Changes from Assignment 7:
- Update `WorksheetModel` to use ` HashMap<Coord, Sexp>` also.
- ADD `getCellsInRow` and `getCellsInColumn` to `Worksheet`.
- ADD `CustomOverlayPanel` replace `SelectionPanel` now it's works better w/o problem w/ big number, will completed remove after make sure we don't need anymore.
- ADD support for column references `(SUM A:B)`.
- ADD `WorkbookBuilder` AND `WorkBook` AND implements `WorkBookImpl` Full Tested.
- ADD `IMultView` AND `IMultController`.
- ADD `WorkBookAdaptorTest`.
- ADD `BeyondGood -in wb.gOOD -multEdit` AND `BeyondGood -multEdit`
- ADD `"BeyondGood -in wb.gOOD -eval-wb A!A1` to BeyondGood