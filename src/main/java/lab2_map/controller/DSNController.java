package lab2_map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lab2_map.domain.*;
import lab2_map.service.SocialNetworkService;
import lab2_map.util.Page;

import java.util.Set;

public class DSNController {

    private SocialNetworkService service;

    // duck table
    @FXML private TableView<Duck> duckTable;
    @FXML private TableColumn<Duck, Long> colId;
    @FXML private TableColumn<Duck, String> colUsername;
    @FXML private TableColumn<Duck, UserType> colType;
    @FXML private TableColumn<Duck, Double> colSpeed;
    @FXML private TableColumn<Duck, Double> colEndurance;

    // paging
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;
    @FXML private Label lblPageNumber;
    private int currentPage = 0;
    private final int PAGE_SIZE = 5;

    // filter
    @FXML private ComboBox<String> comboDuckType;
    @FXML private Button btnApplyFilter;
    private UserType currentFilter = null;

    private final ObservableList<Duck> modelDucks = FXCollections.observableArrayList();

    // remove user
    @FXML private TextField removeUserId;
    @FXML private Button btnRemoveUser;

    // show panes
    @FXML private Button btnViewDucks;
    @FXML private Button btnShowAddPerson;
    @FXML private Button btnShowAddDuck;

    // add person
    @FXML private TitledPane paneAddPerson;
    @FXML private TextField addFirstName;
    @FXML private TextField addLastName;
    @FXML private DatePicker addBirthDate;
    @FXML private TextField addOccupation;
    @FXML private TextField addEmpathy;
    @FXML private TextField addUsernamePerson;
    @FXML private TextField addEmailPerson;
    @FXML private PasswordField addPasswordPerson;
    @FXML private Button btnAddPerson;

    // add duck
    @FXML private TitledPane paneAddDuck;
    @FXML private ComboBox<String> addDuckType;
    @FXML private TextField addSpeed;
    @FXML private TextField addEndurance;
    @FXML private TextField addUsernameDuck;
    @FXML private TextField addEmailDuck;
    @FXML private PasswordField addPasswordDuck;
    @FXML private Button btnAddDuck;

    // friendships
    @FXML private TextField friendU1;
    @FXML private TextField friendU2;
    @FXML private Button btnAddFriend;
    @FXML private TextField removeFriendId;
    @FXML private Button btnRemoveFriend;

    // communities
    @FXML private Button btnCountCommunities;
    @FXML private Button btnSociableCommunity;
    @FXML private TextArea analysisOutput;

    public void setService(SocialNetworkService service) {
        this.service = service;
        initDuckTypes();
        loadPage(0);
    }

    @FXML
    public void initialize() {

        // table mappings
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        colEndurance.setCellValueFactory(new PropertyValueFactory<>("endurance"));

        duckTable.setItems(modelDucks);

        // buttons
        btnViewDucks.setOnAction(e -> loadPage(0));

        btnPrevPage.setOnAction(e -> {
            if (currentPage > 0) loadPage(currentPage - 1);
        });

        btnNextPage.setOnAction(e -> loadPage(currentPage + 1));

        btnApplyFilter.setOnAction(e -> {
            String val = comboDuckType.getValue();
            currentFilter = (val == null || val.equals("ALL")) ? null : UserType.valueOf(val);
            loadPage(0);
        });

        // show panels
        btnShowAddPerson.setOnAction(e -> {
            paneAddPerson.setExpanded(true);
            paneAddDuck.setExpanded(false);
        });

        btnShowAddDuck.setOnAction(e -> {
            paneAddDuck.setExpanded(true);
            paneAddPerson.setExpanded(false);
        });

        // actions
        btnAddPerson.setOnAction(e -> handleAddPerson());
        btnAddDuck.setOnAction(e -> handleAddDuck());
        btnRemoveUser.setOnAction(e -> handleRemoveUser());
        btnAddFriend.setOnAction(e -> handleAddFriend());
        btnRemoveFriend.setOnAction(e -> handleRemoveFriend());
        btnCountCommunities.setOnAction(e -> handleCountCommunities());
        btnSociableCommunity.setOnAction(e -> handleMostSociableCommunity());
    }

    // init duck types in comboboxes
    private void initDuckTypes() {
        comboDuckType.getItems().add("ALL");
        for (UserType t : UserType.values()) comboDuckType.getItems().add(t.name());
        comboDuckType.setValue("ALL");

        for (UserType t : UserType.values()) addDuckType.getItems().add(t.name());
    }

    // pagination load
    private void loadPage(int pageIndex) {
        Page<Duck> p = service.getDucksPage(pageIndex, PAGE_SIZE, currentFilter);

        modelDucks.setAll(p.getContent());
        currentPage = pageIndex;

        lblPageNumber.setText("Page " + (pageIndex + 1));

        int total = p.getTotalCount();
        int maxPage = total == 0 ? 0 : (total - 1) / PAGE_SIZE;

        btnPrevPage.setDisable(currentPage <= 0);
        btnNextPage.setDisable(currentPage >= maxPage);
    }

    // add person
    private void handleAddPerson() {
        try {
            Person p = new Person(
                    null,
                    addUsernamePerson.getText(),
                    addEmailPerson.getText(),
                    addPasswordPerson.getText(),
                    addFirstName.getText(),
                    addLastName.getText(),
                    addBirthDate.getValue(),
                    addOccupation.getText(),
                    Double.parseDouble(addEmpathy.getText())
            );

            service.addUser(p);
            loadPage(currentPage);
            analysisOutput.setText("Person added.");
        } catch (Exception e) {
            analysisOutput.setText("Error: " + e.getMessage());
        }
    }

    // add duck
    private void handleAddDuck() {
        try {
            Duck d = new Duck(
                    null,
                    addUsernameDuck.getText(),
                    addEmailDuck.getText(),
                    addPasswordDuck.getText(),
                    UserType.valueOf(addDuckType.getValue()),
                    Double.parseDouble(addSpeed.getText()),
                    Double.parseDouble(addEndurance.getText())
            );

            service.addUser(d);
            loadPage(currentPage);
            analysisOutput.setText("Duck added.");
        } catch (Exception e) {
            analysisOutput.setText("Error: " + e.getMessage());
        }
    }

    // remove user
    private void handleRemoveUser() {
        try {
            Long id = Long.parseLong(removeUserId.getText());
            service.deleteUser(id);
            loadPage(currentPage);
            analysisOutput.setText("User removed.");
        } catch (Exception e) {
            analysisOutput.setText("Error: " + e.getMessage());
        }
    }

    // friendships
    private void handleAddFriend() {
        try {
            Long u1 = Long.parseLong(friendU1.getText());
            Long u2 = Long.parseLong(friendU2.getText());
            service.addFriendship(u1, u2);
            analysisOutput.setText("Friendship added.");
        } catch (Exception e) {
            analysisOutput.setText("Error: " + e.getMessage());
        }
    }

    private void handleRemoveFriend() {
        try {
            Long id = Long.parseLong(removeFriendId.getText());
            service.deleteFriendship(id);
            analysisOutput.setText("Friendship removed.");
        } catch (Exception e) {
            analysisOutput.setText("Error: " + e.getMessage());
        }
    }

    // communities
    private void handleCountCommunities() {
        try {
            int count = service.getCommunityCount();
            analysisOutput.setText("Communities: " + count);
        } catch (Exception e) {
            e.printStackTrace();
            analysisOutput.setText("Error computing communities: " + e.getMessage());
        }
    }

    private void handleMostSociableCommunity() {
        try {
            Set<User> comm = service.getMostSociableCommunity();

            if (comm == null || comm.isEmpty()) {
                analysisOutput.setText("No communities found.");
                return;
            }

            StringBuilder sb = new StringBuilder("Most sociable community:\n");
            for (User u : comm) {
                if (u != null) {
                    sb.append(u.getUsername()).append("\n");
                }
            }

            analysisOutput.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            analysisOutput.setText("Error computing most sociable community: " + e.getMessage());
        }
    }
}
