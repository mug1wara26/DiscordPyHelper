//This code was taken from the RichTextFX JavaKeywordsDemo

package Model;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxArea {
    private CodeArea codeArea;

    public CodeArea getCodeArea(boolean setIndent) {
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        setIndent(setIndent);

        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.TAB) {
                    codeArea.insertText(codeArea.getCaretPosition(), "    ");
                    e.consume();
                }
            }
        });
        return codeArea;
    }

    private void setIndent(boolean bool) {
        if(bool) {
            final Pattern whiteSpace = Pattern.compile("^\\s+");
            codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
            {
                final String codeAreaText = codeArea.getText();
                if (KE.getCode() == KeyCode.ENTER) {
                    int caretPosition = codeArea.getCaretPosition();
                    int currentParagraph = codeArea.getCurrentParagraph();
                    Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                    if (m0.find()) codeArea.insertText(caretPosition, m0.group());

                    try {
                        if (codeAreaText.substring(codeAreaText.length() - 2, codeAreaText.length() - 1).equals(":")) {
                            int codeAreaTextIndex = codeAreaText.length() - 3;
                            while (codeAreaTextIndex != 0 && (codeAreaText.charAt(codeAreaTextIndex) + "").equals("\n")) {
                                codeAreaTextIndex--;
                            }

                            codeAreaTextIndex += 1;
                            System.out.println("A" +codeAreaTextIndex);
                            StringBuilder indentLevel = new StringBuilder();
                            while (("" + codeAreaText.charAt(codeAreaTextIndex)).equals(" ")) {
                                indentLevel.append(" ");
                                codeAreaTextIndex++;
                            }

                            codeArea.insertText(codeArea.getCaretPosition(), "    " + indentLevel.toString());
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
            });
        }
    }
}