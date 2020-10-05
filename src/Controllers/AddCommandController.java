package Controllers;

import Model.Check;
import Model.Command;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommandController implements Initializable {
    @FXML
    private TextField commandNameTF;
    @FXML
    private TextField commandDescTF;

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
    private ComboBox<String> defaultChecksCB;
    @FXML
    private VBox editCheckVBox;
    @FXML
    private ComboBox<String> addedChecksCB;
    @FXML
    private HBox haveChecksHBox;
    @FXML
    private CheckBox checkAnyCheckBox;

    private TextField inputParamTF;

    DefaultChecks defaultChecks = new DefaultChecks();
    private String helpChecksLink = "https://discordpy.readthedocs.io/en/latest/ext/commands/api.html#checks";
    private ArrayList<TextField> requiredFields = new ArrayList<>();
    private ArrayList<Check> checks = new ArrayList<>();
    ArrayList<CheckBox> permissionsCheckBox = new ArrayList<>();
    String paramsType;


    //Variables required for cancel/add command
    @FXML
    private Button cancelBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        discordConvDefaultNames = new String[discordConvNames.length];

        for(int i = 0; i < discordConvNames.length; i++) {
            discordConvDefaultNames[i] = discordConvNames[i].substring(0, 1).toLowerCase() + discordConvNames[i].substring(1);
        }

        //ARGUMENTS TAB
        //Initialize tooltips for help hyperlink
        Tooltip helpArgToolTip = new Tooltip(helpArgLink);
        helpArgToolTip.setShowDelay(Duration.seconds(0.3));
        helpArgHL.setTooltip(helpArgToolTip);

        //Initialize PosArgSpinner
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99);
        posArgSpinner.setValueFactory(svf);


        //CONVERTERS TAB
        //Initialize tooltips for help hyperlink
        Tooltip helpConverterToolTip = new Tooltip(helpConverterLink);
        helpArgToolTip.setShowDelay(Duration.seconds(0.3));
        helpConvHL.setTooltip(helpConverterToolTip);

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

    }

    private void varArgVis(boolean b) {
        varKeywordVBox.setVisible(b);
        varKeywordVBox.setDisable(!b);
    }

    //Handler for help hyperlinks
    @FXML
    public void handleHelpArgHL(ActionEvent e) throws URISyntaxException, IOException {
        handleHyperlink(helpArgHL, helpArgLink);
    }

    //Handlers for Radio Buttons
    @FXML
    public void handlePositionalRB(ActionEvent e) {
        variableRB.setSelected(false);
        if (positionalRB.isSelected()) {
            posArgVis(true);
            varArgVis(false);
        }
        else posArgVis(false);
    }

    @FXML
    public void handleVariableRB(ActionEvent e) {
        positionalRB.setSelected(false);
        if(variableRB.isSelected()) {
            posArgVis(false);
            varArgVis(true);
        }
        else varArgVis(false);
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
        basicConvRB.setSelected(false);
        if(discordConvRB.isSelected()) {
            discordConvVBox.setVisible(true);
            basicConvVBox.setVisible(false);

            discordConvVBox.setDisable(false);
            basicConvVBox.setDisable(true);
        }
        else {
            discordConvVBox.setVisible(false);
            discordConvVBox.setDisable(true);
        }
    }

    @FXML
    public void handleBasicConvRB(ActionEvent e) {
        discordConvRB.setSelected(false);
        if(basicConvRB.isSelected()) {
            basicConvVBox.setVisible(true);
            discordConvVBox.setVisible(false);

            basicConvVBox.setDisable(false);
            discordConvVBox.setDisable(true);
        }
        else {
            basicConvVBox.setVisible(false);
            basicConvVBox.setDisable(true);
        }
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



    //ERROR HANDLING TAB=
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



    //CHECKS TAB
    // Method for parsing the default checks
    private void parseCheckName(String checkName) {
        editCheckVBox.getChildren().clear();

        String params = defaultChecks.getDefaultParams(checkName);
        requiredFields.clear();
        permissionsCheckBox.clear();

        if(params == null) {
            return;
        }

        paramsType = params.substring(0, params.indexOf("["));
        String paramContent = params.substring(params.indexOf("[") + 1, params.length() - 1);
        Label infoLbl;
        String[] paramContentTokens;


        Hyperlink checkHelpHL = new Hyperlink();
        checkHelpHL.setText("Help");
        Tooltip checkHelpToolTip = new Tooltip(defaultChecks.getURL(checkName));
        checkHelpToolTip.setShowDelay(Duration.seconds(0.3));
        checkHelpHL.setTooltip(checkHelpToolTip);

        checkHelpHL.setOnAction(e -> {
            try {
                handleHyperlink(checkHelpHL, defaultChecks.getURL(checkName));
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });

        editCheckVBox.getChildren().add(checkHelpHL);
        VBox.setMargin(checkHelpHL, new Insets(0, 0, 10, 0));


        switch (paramsType) {
            case "Union":
                paramContentTokens = paramContent.split("[,]");
                infoLbl = new Label();
                infoLbl.setText(produceInfoLbl("Insert parameter for " + checkName +" check, it can only be a ", paramContentTokens) + "Note that everything in the TextField is taken literally, include your literals.");
                infoLbl.setWrapText(true);

                inputParamTF = new TextField();
                inputParamTF.setPromptText("Insert Parameter");

                editCheckVBox.getChildren().addAll(infoLbl, inputParamTF);
                VBox.setMargin(inputParamTF, new Insets(10, 0, 0, 0));

                requiredFields.add(inputParamTF);
                break;
            case "List":
                String innerParam = paramContent.substring(paramContent.indexOf("[") + 1, paramContent.length() - 1);
                String[] innerParamTokens = innerParam.split("[,]");

                infoLbl = new Label();
                infoLbl.setText(produceInfoLbl("Insert parameter for " + checkName +" check, it is a list that can only contain ", innerParamTokens) + ". Note that you need not include the list literal but everything else in the textfield will be taken literally.");
                infoLbl.setWrapText(true);

                inputParamTF = new TextField();
                inputParamTF.setPromptText("Insert List Parameter");

                editCheckVBox.getChildren().addAll(infoLbl, inputParamTF);
                VBox.setMargin(inputParamTF, new Insets(10, 0, 0, 0));

                requiredFields.add(inputParamTF);
                break;
            case "args":
                infoLbl = new Label();
                infoLbl.setText("Insert arguments for " + checkName + " check.");
                infoLbl.setWrapText(true);

                editCheckVBox.getChildren().add(infoLbl);

                paramContentTokens = paramContent.split("[,]");
                for(String arg : paramContentTokens) {
                    TextField inputCheckArgTF = new TextField();
                    inputCheckArgTF.setPromptText(arg);
                    editCheckVBox.getChildren().add(inputCheckArgTF);
                    VBox.setMargin(inputCheckArgTF, new Insets(10, 0, 0, 0));

                    requiredFields.add(inputCheckArgTF);
                }

                break;
            case "kwargs":
                if(paramContent.equals("Permissions")) {
                    VBox permissionRoot = new VBox();

                    ScrollPane permissionScrollPane = new ScrollPane();
                    permissionScrollPane.setPrefHeight(250);
                    permissionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    permissionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    permissionScrollPane.setContent(permissionRoot);

                    String[] permissionsArr = new String[]{"create_instant_invite", "kick_members", "ban_members", "administrator", "manage_channels", "manage_guild", "add_reactions", "view_audit_log", "priority_speaker", "stream", "read_messages", "view_channel", "send_messages", "send_tts_messages", "manage_messages", "manage_messages", "attach_files", "read_message_history", "mention_everyone", "external_emojis", "use_external_emojis", "view_guild_insights", "connect", "speak", "mute_members", "deafen_members", "move_members", "use_voice_activation", "change_nickname", "manage_nicknames", "manage_roles", "manage_permissions", "manage_webhooks", "manage_emojis"};

                    for(String permission : permissionsArr) {
                        CheckBox permissionCheckBox = new CheckBox();
                        permissionCheckBox.setText(permission);

                        VBox.setMargin(permissionCheckBox, new Insets(2, 0, 0, 0));

                        permissionsCheckBox.add(permissionCheckBox);
                        permissionRoot.getChildren().add(permissionCheckBox);
                    }


                    permissionRoot.setStyle("-fx-background-color: #3f3f3f");
                    permissionRoot.setPrefWidth(400);
                    permissionScrollPane.getStyleClass().add("checks-scroll-pane");
                    permissionScrollPane.getStyleClass().add("edge-to-edge");

                    editCheckVBox.getChildren().add(permissionScrollPane);
                }
                break;
        }
    }

    private String produceInfoLbl(String initString, String[] paramContentTokens) {
        StringBuilder infoLblText = new StringBuilder(initString);
        for(int i = 0; i < paramContentTokens.length; i++) {
            infoLblText.append(paramContentTokens[i]);
            if(i < paramContentTokens.length - 2) infoLblText.append(", ");
            else if(i == paramContentTokens.length - 2) infoLblText.append(" or ");
        }

        return infoLblText.toString();
    }


    @FXML
    public void handleHelpChecksHL(ActionEvent e) throws IOException, URISyntaxException {
        handleHyperlink(helpChecksHL, helpChecksLink);
    }

    @FXML
    public void handleHaveChecksRB(ActionEvent e) {
        haveChecksHBox.setVisible(true);
        haveChecksHBox.setDisable(false);
    }

    @FXML
    public void handleNoChecksRB(ActionEvent e) {
        haveChecksHBox.setVisible(false);
        haveChecksHBox.setDisable(true);
    }

    @FXML
    public void handleDefaultChecksCB(ActionEvent e) {
        if(defaultChecksCB.getValue() == null) return;


        parseCheckName(defaultChecksCB.getValue());
    }

    @FXML
    public void handleAddDefaultCheckBtn(ActionEvent e) {
        if(defaultChecksCB.getValue() == null) {
            Main.alert(Alert.AlertType.ERROR, "Choose a check!");
            return;
        }

        if(addedChecksCB.getItems().contains(defaultChecksCB.getValue())) {
            Main.alert(Alert.AlertType.ERROR, "Check has already been added!");
            return;
        }

        boolean requiredFieldsFilled = true;
        for(TextField textField : requiredFields) {
            if(textField.getText().isEmpty()) {
                requiredFieldsFilled = false;
                break;
            }
        }

        if(!requiredFieldsFilled) {
            Main.alert(Alert.AlertType.ERROR, "Please fill in all required fields!");
            return;
        }

        addedChecksCB.getItems().add(defaultChecksCB.getValue());
        Check check = new Check(defaultChecksCB.getValue(), paramsType);

        if(requiredFields.isEmpty() || permissionsCheckBox.isEmpty()) {
            if (!requiredFields.isEmpty()) {
                for (TextField tf : requiredFields) {
                    check.addParams(tf.getText());
                }
            }
            if (!permissionsCheckBox.isEmpty()) {
                for (CheckBox cb : permissionsCheckBox) {
                    if(cb.isSelected()) check.addParams(cb.getText() + "=" + (cb.isSelected() + "").substring(0, 1).toUpperCase() + ("" + cb.isSelected()).substring(1));
                }
            }

            checks.add(check);
        }
    }

    @FXML
    public void handleRemoveDefaultCheckBtn(ActionEvent e) {
        addedChecksCB.getItems().remove(addedChecksCB.getValue());
    }


    //Cancel or add command
    @FXML
    public void handleCancelBtn(ActionEvent e) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleNextBtn(ActionEvent e) throws IOException {
        if(commandNameTF.getText().isEmpty() || commandDescTF.getText().isEmpty()) {
            Main.alert(Alert.AlertType.ERROR, "Enter your command name and command description!");
            return;
        }

        Command command = new Command(commandNameTF.getText(), commandDescTF.getText());
        command.setChecks(checks);
        command.setCheckAny(checkAnyCheckBox.isSelected());

        FinishAddCommandController.setCommand(command);

        Main.changeScene("/View/finishAddCommand.fxml");
    }
}