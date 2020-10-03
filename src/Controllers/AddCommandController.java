package Controllers;

import Model.DefaultChecks;
import Model.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommandController implements Initializable {
    //Variables required for Arguments tab
    @FXML
    private RadioButton positionalRB;
    @FXML
    private RadioButton variableRB;
    @FXML
    private ComboBox<String> posArgNameCB;
    @FXML
    private Spinner<Integer> posArgSpinner;
    @FXML
    private Hyperlink helpArgHL;
    @FXML
    private Button posArgNameBtn;
    @FXML
    private TextField posArgDefaultValTF;
    @FXML
    private VBox posArgVBox;
    @FXML
    private VBox varKeywordVBox;
    @FXML
    private RadioButton varArgRB;
    @FXML
    private RadioButton keywordOnlyRB;

    private ObservableList<String> posArgsNames;
    private String helpArgLink = "https://discordpy.readthedocs.io/en/latest/ext/commands/commands.html#positional";
    private String varKeywordArgName;


    //Variables required for Converters tab
    @FXML
    private Hyperlink helpConvHL;
    @FXML
    private VBox discordConvVBox;
    @FXML
    private VBox basicConvVBox;
    @FXML
    private RadioButton discordConvRB;
    @FXML
    private RadioButton basicConvRB;
    @FXML
    private ComboBox<String> discordConvCB;
    @FXML
    private ComboBox<String> addedDiscordConvCB;
    @FXML
    private ComboBox<String> basicConvCB;
    @FXML
    private ComboBox<String> addedBasicConvCB;

    private String helpConverterLink = "https://discordpy.readthedocs.io/en/latest/ext/commands/commands.html#converters";

    //Discord converter vars
    private String[] discordConvNames = new String[]{"Member", "Message", "User", "TextChannel", "VoiceChannel", "CategoryChannel", "Role", "Invite", "Game", "Emoji", "PartialEmoji", "Colour"};
    private String[] discordConvDefaultNames;
    private int[] discordConvCount = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};

    //Basic converter vars
    private ArrayList<String> basicConvNames = new ArrayList<>();
    private ArrayList<String> basicConvDefaultNames = new ArrayList<>();
    private ArrayList<Integer> basicConvCount = new ArrayList<>();
    int[] basicConvCountArr = new int[basicConvCount.size()];


    //Variables required for Error Handling
    @FXML
    private Hyperlink helpErrHL;
    @FXML
    private RadioButton haveErrRB;
    @FXML
    private RadioButton noErrRB;
    @FXML
    private ComboBox<String> errHandlerCB;
    @FXML
    private ComboBox<String> addedErrHandlerCB;
    @FXML
    private VBox errHandlerVBox;

    private String helpErrLink = "https://discordpy.readthedocs.io/en/latest/ext/commands/commands.html#error-handling";
    private String[] errorHandlers = new String[]{"DiscordException", "CommandError", "ConversionError", "UserInputError", "MissingRequiredArgument", "TooManyArguments", "BadArgument", "MessageNotFound", "MemberNotFound", "UserNotFoun", "ChannelNotFound", "ChannelNotReadable", "BadColourArgument", "RoleNotFound", "BadInviteArgument", "EmojiNotFound", "PartialEmojiConversionFailure", "BadBoolArgument", "BadUnionArgument", "ArgumentParsingError", "UnexpectedQuoteError", "InvalidEndOfQuotedStringError", "ExpectedClosingQuoteError", "CommandNotFound", "CheckFailure", "CheckAnyFailure ", "PrivateMessageOnly", "NoPrivateMessage", "NotOwner", "MissingPermissions", "BotMissingPermissions", "MissingRole", "BotMissingRole", "MissingAnyRole", "BotMissingAnyRole", "NSFWChannelRequired", "DisabledCommand", "CommandInvokeError", "CommandOnCooldown", "MaxConcurrencyReached", "ExtensionError", "ExtensionAlreadyLoaded", "ExtensionNotLoaded", "NoEntryPointError", "ExtensionFailed", "ExtensionNotFound", "ClientException", "CommandRegistrationError"};


    //Variables required for checks
    @FXML
    private RadioButton haveChecksRB;
    @FXML
    private RadioButton noChecksRB;
    @FXML
    private Hyperlink helpChecksHL;
    @FXML
    private Button addDefaultCheckBtn;
    @FXML
    private ComboBox<String> defaultChecksCB;
    @FXML
    private VBox editCheckVBox;
    private TextField inputParamTF;

    DefaultChecks defaultChecks = new DefaultChecks();
    private String helpChecksLink = "https://discordpy.readthedocs.io/en/latest/ext/commands/api.html#checks";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        discordConvDefaultNames = new String[discordConvNames.length];

        for(int i = 0; i < discordConvNames.length; i++) {
            discordConvDefaultNames[i] = discordConvNames[i].substring(0, 1).toLowerCase() + discordConvNames[i].substring(1);
        }

        //ARGUMENTS TAB
        //Setting ToggleGroup for radio buttons
        ToggleGroup argGroup = new ToggleGroup();
        positionalRB.setToggleGroup(argGroup);
        variableRB.setToggleGroup(argGroup);

        positionalRB.setSelected(true);

        //Initialize tooltips for help hyperlink
        Tooltip helpArgToolTip = new Tooltip(helpArgLink);
        helpArgToolTip.setShowDelay(Duration.seconds(0.3));
        helpArgHL.setTooltip(helpArgToolTip);

        //Initialize PosArgSpinner
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99);
        posArgSpinner.setValueFactory(svf);

        //Initialize posArgs
        posArgVis(true);


        //CONVERTERS TAB
        //Initialize tooltips for help hyperlink
        Tooltip helpConverterToolTip = new Tooltip(helpConverterLink);
        helpArgToolTip.setShowDelay(Duration.seconds(0.3));
        helpConvHL.setTooltip(helpConverterToolTip);

        //Set ToggleGroup for radio buttons
        ToggleGroup convGroup = new ToggleGroup();
        discordConvRB.setToggleGroup(convGroup);
        basicConvRB.setToggleGroup(convGroup);
        discordConvRB.setSelected(true);
        discordConvVBox.setVisible(true);
        discordConvVBox.setDisable(false);

        //Initialize ComboBoxes
        discordConvCB.getItems().addAll(discordConvNames);


        //ERROR HANDLING TAB
        //Initialize tooltips for help hyperlink
        Tooltip helpErrToolTip = new Tooltip(helpErrLink);
        helpArgToolTip.setShowDelay(Duration.seconds(0.3));
        helpErrHL.setTooltip(helpErrToolTip);

        //Set ToggleGroup for radio buttons
        ToggleGroup errGroup = new ToggleGroup();
        haveErrRB.setToggleGroup(errGroup);
        noErrRB.setToggleGroup(errGroup);
        haveErrRB.setSelected(true);

        //Setting up Error Handlers CB
        Arrays.sort(errorHandlers);
        errHandlerCB.getItems().addAll(errorHandlers);


        //CHECKS TAB
        //Initialize tooltips for help hyperlink
        Tooltip helpChecksToolTip = new Tooltip(helpChecksLink);
        helpChecksToolTip.setShowDelay(Duration.seconds(0.3));
        helpChecksHL.setTooltip(helpChecksToolTip);

        //Set ToggleGroup for radio buttons
        ToggleGroup checksGroup = new ToggleGroup();
        haveChecksRB.setToggleGroup(checksGroup);
        noChecksRB.setToggleGroup(checksGroup);
        haveChecksRB.setSelected(true);

        //Set up Combo Box
        defaultChecksCB.getItems().addAll(defaultChecks.getDefaultChecks());
    }

    //Method to make setting hyperlinks easier
    private void handleHyperlink(Hyperlink hyperlink, String uri) throws URISyntaxException, IOException {
        hyperlink.setVisited(false);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(uri));
        }
    }


    //ARGUMENTS TAB
    //Method to change which controls are visible when clicking on radio buttons
    private void posArgVis(boolean b) {
        posArgVBox.setVisible(b);
        posArgVBox.setDisable(!b);

        varKeywordVBox.setVisible(!b);
        varKeywordVBox.setDisable(b);
    }

    //Handler for help hyperlinks
    @FXML
    public void handleHelpArgHL(ActionEvent e) throws URISyntaxException, IOException {
        handleHyperlink(helpArgHL, helpArgLink);
    }

    //Handlers for Radio Buttons
    @FXML
    public void handlePositionalRB(ActionEvent e) {
        posArgVis(true);
    }

    @FXML
    public void handleVariableRB(ActionEvent e) {
        posArgVis(false);
    }

    @FXML
    public void handleVarArgRB(ActionEvent e) {
        keywordOnlyRB.setSelected(false);
        if(varArgRB.isSelected()) varKeywordArgName = "varArg";
    }

    @FXML
    public void handleKeywordOnlyRB(ActionEvent e) {
        varArgRB.setSelected(false);
        if(keywordOnlyRB.isSelected()) varKeywordArgName = "keywordArg";
    }

    @FXML
    public void posArgNameCBME(MouseEvent me) {
        if(posArgNameCB.isShowing()) {
            posArgsNames = posArgNameCB.getItems();
            if(posArgSpinner.getValue() < posArgsNames.size()) posArgsNames.remove(posArgSpinner.getValue(), posArgsNames.size());
            else {
                for (int i = posArgsNames.size(); i < posArgSpinner.getValue(); i++) {
                    if (posArgSpinner.getValue() == 1) posArgsNames.add("arg");
                    else posArgsNames.add("arg" + (i + 1));
                }
            }
        }

        posArgNameCB.setItems(posArgsNames);
    }

    @FXML
    public void handlePosArgNameCB(ActionEvent e) {
        String posArgName = posArgNameCB.getValue();
        if(posArgName != null) {
            posArgNameBtn.setDisable(false);
        }
        else {
            posArgNameBtn.setDisable(true);
        }
    }

    @FXML
    public void handlePosArgNameBtn(ActionEvent e) {
        String posArgName = posArgNameCB.getValue();
        for (int i = 0; i < posArgsNames.size(); i++) {
            if (posArgsNames.get(i).equals(posArgNameCB.getValue())) {
                posArgsNames.remove(i);
                if(!posArgDefaultValTF.getText().isEmpty()) posArgName += " = " + posArgDefaultValTF.getText();
                else if(posArgName.contains(" ")) posArgName = posArgName.substring(0, posArgName.indexOf(" "));

                posArgsNames.add(i, posArgName);
                posArgNameBtn.setDisable(true);
            }
        }
        posArgNameCB.setItems(posArgsNames);
    }



    //CONVERTERS TAB
    //Methods for handling ComboBoxes
    private int getIndex(String[] convNames, int[] convCount, String s) {
        int index = 0;
        for (String conv : convNames) {
            if (conv.equals(s)) break;
            index++;
        }

        return index;
    }

    private int getSum(int[] convCount, int index) {
        int sum = 0;
        for(int i = 0; i <= index; i++) {
            sum += convCount[i];
        }

        return sum;
    }

    private void addedConverterAddItem(ComboBox<String> convCB, ComboBox<String> addedConvCB, String[] convNames, int[] convCount) {
        String convCBValue = convCB.getValue();

        int index = 0;
        if(!convCBValue.isEmpty()) {
            index = getIndex(convNames, convCount, convCBValue);
        }
        else return;

        int sum = getSum(convCount, index);

        String defaultName = convCB.getValue().substring(0, 1).toLowerCase() + convCB.getValue().substring(1);
        if(addedConvCB.getItems().contains(defaultName)) {
            addedConvCB.getItems().remove(sum - 1);
            addedConvCB.getItems().add(sum - 1, defaultName + 1);
            addedConvCB.getItems().add(sum, defaultName + 2);
        }
        else {
            if(addedConvCB.getItems().contains(defaultName + 1)) addedConvCB.getItems().add(sum, defaultName + (convCount[index] + 1));
            else addedConvCB.getItems().add(sum, defaultName);
        }
        convCount[index]++;
    }

    private void addedConverterRemoveItem(ComboBox<String> addedConvCB, String[] convDefaultNames, int[] convCount) {
        if(addedConvCB.getValue() == null) {
            Main.alert(Alert.AlertType.ERROR, "Select a converter to remove!");
            return;
        }

        int index = 0;
        for(int i = 0; i < addedConvCB.getItems().size(); i++) {
            if(addedConvCB.getItems().get(i).equals(addedConvCB.getValue())) {
                index = i;
                break;
            }
        }

        int convIndex = 0;
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(addedConvCB.getValue());
        if (matcher.find()) {
            int endIndex = matcher.start();
            boolean defaultNameExists = false;
            while(!defaultNameExists) {
                for (int i = 0; i < convDefaultNames.length; i++) {
                    if (convDefaultNames[i].equals(addedConvCB.getValue().substring(0, endIndex))) {
                        convIndex = i;
                        defaultNameExists = true;
                        break;
                    }
                }

                if(defaultNameExists) break;

                if(endIndex != addedConvCB.getValue().length() - 1)  endIndex++;
                else {
                    convCount[getIndex(convDefaultNames, convCount, addedConvCB.getValue())]--;
                    addedConvCB.getItems().remove(addedConvCB.getValue());
                    return;
                }
            }

            if(convCount[convIndex] != 2) {
                addedConvCB.getItems().add(index, addedConvCB.getItems().get(index));
                addedConvCB.getItems().remove((convDefaultNames[convIndex] + convCount[convIndex]));
                if(addedConvCB.getItems().size() == index) addedConvCB.getItems().remove(index - 1);
                else addedConvCB.getItems().remove(index);
            }
            else {
                addedConvCB.getItems().add(index, convDefaultNames[convIndex]);
                addedConvCB.getItems().remove(convDefaultNames[convIndex] + 1);
                addedConvCB.getItems().remove(convDefaultNames[convIndex] + 2);
            }
        }
        else addedConvCB.getItems().remove(addedConvCB.getValue());
        convCount[convIndex]--;
    }


    //Handler for hyperlink
    @FXML
    public void handleHelpConverterHL(ActionEvent e) throws IOException, URISyntaxException {
        handleHyperlink(helpConvHL, helpConverterLink);
    }

    //Handler for RB
    @FXML
    public void handleDiscordConvRB(ActionEvent e) {
        discordConvVBox.setVisible(true);
        basicConvVBox.setVisible(false);

        discordConvVBox.setDisable(false);
        basicConvVBox.setDisable(true);
    }

    @FXML
    public void handleBasicConvRB(ActionEvent e) {
        basicConvVBox.setVisible(true);
        discordConvVBox.setVisible(false);

        basicConvVBox.setDisable(false);
        discordConvVBox.setDisable(true);

    }


    @FXML
    public void handleAddDiscordConvBtn(ActionEvent e) {
        addedConverterAddItem(discordConvCB, addedDiscordConvCB, discordConvNames, discordConvCount);
    }

    @FXML
    public void handleRemoveDiscordConvBtn(ActionEvent e) {
        addedConverterRemoveItem(addedDiscordConvCB, discordConvDefaultNames, discordConvCount);
    }

    @FXML
    public void handleAddBasicConvBtn(ActionEvent e) {
        int[] basicConvCountArr = new int[basicConvCount.size()];
        Iterator<Integer> iterator = basicConvCount.iterator();
        for (int i = 0; i < basicConvCountArr.length; i++)
        {
            basicConvCountArr[i] = iterator.next().intValue();
        }

        addedConverterAddItem(basicConvCB, addedBasicConvCB, basicConvNames.toArray(new String[basicConvNames.size()]), basicConvCountArr);

        for(int i = 0; i < basicConvCountArr.length; i++) {
            basicConvCount.set(i, basicConvCountArr[i]);
        }
    }

    @FXML
    public void handleRemoveBasicConvBtn(ActionEvent e) {
        int[] basicConvCountArr = new int[basicConvCount.size()];
        Iterator<Integer> iterator = basicConvCount.iterator();
        for (int i = 0; i < basicConvCountArr.length; i++)
        {
            basicConvCountArr[i] = iterator.next().intValue();
        }

        addedConverterRemoveItem(addedBasicConvCB, basicConvDefaultNames.toArray(new String[basicConvDefaultNames.size()]), basicConvCountArr);

        for(int i = 0; i < basicConvCountArr.length; i++) {
            basicConvCount.set(i, basicConvCountArr[i]);
        }
    }



    //ERROR HANDLING TAB
    //Method for parsing the default checks
    private void parseCheckName(String checkName) {
        String params = defaultChecks.getDefaultParams(checkName);

        if(params == null) {
            Main.alert(Alert.AlertType.ERROR, "Select a check!");
            return;
        }

        editCheckVBox.getChildren().clear();
        String paramsType = params.substring(0, params.indexOf("["));
        String paramContent = params.substring(params.indexOf("[") + 1, params.length() - 1);

        switch (paramsType) {
            case "Union":
                String[] paramContentTokens = paramContent.split("[,]");
                Label infoLbl = new Label();
                StringBuilder infoLblText = new StringBuilder("Insert parameter for " + checkName +" check, it can only be a ");
                for(int i = 0; i < paramContentTokens.length; i++) {
                    infoLblText.append(paramContentTokens[i]);
                    if(i < paramContentTokens.length - 2) infoLblText.append(", ");
                    else if(i == paramContentTokens.length - 2) infoLblText.append(" or ");
                }
                infoLbl.setText(infoLblText.toString());
                infoLbl.setWrapText(true);

                inputParamTF = new TextField();

                editCheckVBox.getChildren().addAll(infoLbl, inputParamTF);
                VBox.setMargin(inputParamTF, new Insets(10, 0, 0, 0));

                break;
            case "List":
                break;
            case "args":
                break;
            case "kwargs":
                break;
        }
    }



    @FXML
    public void handleHelpErrHL(ActionEvent e) throws IOException, URISyntaxException {
        handleHyperlink(helpErrHL, helpErrLink);
    }

    @FXML
    public void handleHaveErrRB(ActionEvent e) {
        errHandlerVBox.setVisible(true);
        errHandlerVBox.setDisable(false);
    }

    @FXML
    public void handleNoErrRB(ActionEvent e) {
        errHandlerVBox.setVisible(false);
        errHandlerVBox.setDisable(true);
    }

    @FXML
    public void handleAddErrHandler(ActionEvent e) {
        if(errHandlerCB.getValue() == null) {
            Main.alert(Alert.AlertType.ERROR, "Select an error handler to add!");
            return;
        }

        addedErrHandlerCB.getItems().add(errHandlerCB.getValue());
        addedErrHandlerCB.getItems().sort(Comparator.naturalOrder());
        errHandlerCB.getItems().remove(errHandlerCB.getValue());
    }

    @FXML
    public void handleRemoveErrHandler(ActionEvent e) {
        if(addedErrHandlerCB.getValue() == null) {
            Main.alert(Alert.AlertType.ERROR, "Select an error handler to remove!");
            return;
        }

        errHandlerCB.getItems().add(addedErrHandlerCB.getValue());
        errHandlerCB.getItems().sort(Comparator.naturalOrder());
        addedErrHandlerCB.getItems().remove(addedErrHandlerCB.getValue());
    }

    @FXML
    public void handleHelpChecksHL(ActionEvent e) throws IOException, URISyntaxException {
        handleHyperlink(helpChecksHL, helpChecksLink);
    }

    @FXML
    public void handleHaveChecksRB(ActionEvent e) {
    }

    @FXML
    public void handleNoChecksRB(ActionEvent e) {
    }

    @FXML
    public void handleDefaultChecksCB(ActionEvent e) {
        if(defaultChecksCB.getValue() == null) {
            Main.alert(Alert.AlertType.ERROR, "Pick a check!");
            return;
        }
        parseCheckName(defaultChecksCB.getValue());
    }
}