//This code was taken from the RichTextFX JavaKeywordsDemo

package Model;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonSyntaxArea {
    private CodeArea codeArea;

    public PythonSyntaxArea() {
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            final String codeAreaText = codeArea.getText();
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group()));

                try {
                    if (codeAreaText.substring(codeAreaText.length() - 2, codeAreaText.length() - 1).equals(":")) {
                        System.out.println("here");
                        codeArea.insertText(codeArea.getCaretPosition(), "    ");
                    }
                }
                catch (IndexOutOfBoundsException ignored) {}
            }
        });


        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.TAB) {
                    codeArea.insertText(codeArea.getCaretPosition(), "    ");
                    e.consume();
                }
            }
        });

        codeArea.setStyle("-fx-background-color: #1f1f1f");
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }


}