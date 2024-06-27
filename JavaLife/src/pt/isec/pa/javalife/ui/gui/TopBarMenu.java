package pt.isec.pa.javalife.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import pt.isec.pa.javalife.model.SimuladorManager;
import pt.isec.pa.javalife.ui.gui.resources.ImageManager;

import java.io.File;
import java.util.Optional;

public class TopBarMenu extends MenuBar {
    SimuladorManager simuladorManager;
    Menu mnFicheiro, mnEcossistema, mnSimulacao, mnEvento;
    MenuItem mnCreate, mnOpen, mnSave, mnExport, mnImport, mnExit, mnConfig, mnAddInaninamdo, mnAddFlora, mnAddFauna, mnEdit, mnDelete, mnUndo, mnRedo, mnConfigSim, mnPause, mnSaveSnap, mnRestoreSanap, mnAddSol, mnApplyHerb, mnInForca;

    public TopBarMenu(SimuladorManager simuladorManager) {
        this.simuladorManager = simuladorManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        mnFicheiro = new Menu("Ficheiro");
        mnEcossistema = new Menu("Ecossistema");
        mnSimulacao = new Menu("Simulação");
        mnEvento = new Menu("Evento");

        mnCreate = new MenuItem("Criar");
        mnOpen = new MenuItem("Abrir");
        mnSave = new MenuItem("Gravar");
        mnExport = new MenuItem("Exportar");
        mnImport = new MenuItem("Importar");
        mnExit = new MenuItem("Sair");

        mnConfig = new MenuItem("Configuração");
        mnAddInaninamdo = new MenuItem("Adicionar Inanimado");
        mnAddInaninamdo.setAccelerator(new KeyCodeCombination(KeyCode.I));
        mnAddFlora = new MenuItem("Adicionar Flora");
        mnAddFlora.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        mnAddFauna = new MenuItem("Adicionar Fauna");
        mnAddFauna.setAccelerator(new KeyCodeCombination(KeyCode.F));
        mnEdit = new MenuItem("Editar");
        mnDelete = new MenuItem("Eliminar");

        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");

        mnConfigSim = new MenuItem("Configuração");
        mnPause = new MenuItem("Pausar");
        mnSaveSnap = new MenuItem("Guardar SnapShot");
        mnRestoreSanap = new MenuItem("Restaurar SnapShot");

        mnAddSol = new MenuItem("Adicionar Sol");
        mnApplyHerb = new MenuItem("Aplicar Herbicida");
        mnInForca = new MenuItem("Injetar Força");

        mnFicheiro.getItems().addAll(mnCreate, mnOpen, mnSave, mnExport, mnImport, mnExit);
        mnEcossistema.getItems().addAll(mnConfig, mnAddInaninamdo, mnAddFlora, mnAddFauna, mnEdit, mnDelete,mnUndo, mnRedo);
        mnSimulacao.getItems().addAll(mnConfigSim, mnPause, mnSaveSnap, mnRestoreSanap);
        mnEvento.getItems().addAll(mnAddSol, mnApplyHerb, mnInForca);

        this.getMenus().addAll(mnFicheiro, mnEcossistema, mnSimulacao, mnEvento);
    }

    private void registerHandlers() {
        //Ficheiro

        mnCreate.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Editar configurações");
            dialog.setHeaderText("Introduza os seguintes detalhes:");

            // Set the button types.
            ButtonType submitButtonType = new ButtonType("Submit", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            // Create the input fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField forceDefaultFloraField = new TextField();
            forceDefaultFloraField.setPromptText("Força default [Flora]");
            TextField gainForceFaunaField = new TextField();
            gainForceFaunaField.setPromptText("Ganho força [Fauna - alimento]");
            TextField lossForceFloraField = new TextField();
            lossForceFloraField.setPromptText("Perda força [Flora - alimento]");
            TextField forceDefaultFaunaField = new TextField();
            forceDefaultFaunaField.setPromptText("Força default [Fauna]");
            TextField lossForceFaunaMoveField = new TextField();
            lossForceFaunaMoveField.setPromptText("Perda de força [Fauna - movimento]");

            grid.add(new Label("Força default [Flora]:"), 0, 0);
            grid.add(forceDefaultFloraField, 1, 0);
            grid.add(new Label("Ganho força [Fauna - alimento]:"), 0, 1);
            grid.add(gainForceFaunaField, 1, 1);
            grid.add(new Label("Perda força [Flora - alimento]:"), 0, 2);
            grid.add(lossForceFloraField, 1, 2);
            grid.add(new Label("Força default [Fauna]:"), 0, 3);
            grid.add(forceDefaultFaunaField, 1, 3);
            grid.add(new Label("Perda de força [Fauna - movimento]:"), 0, 4);
            grid.add(lossForceFaunaMoveField, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // Request focus on the first field by default.
            Platform.runLater(() -> forceDefaultFloraField.requestFocus());

            // Handle the result.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    System.out.println("Força default [Flora]: " + forceDefaultFloraField.getText());
                    System.out.println("Ganho força [Fauna - alimento]: " + gainForceFaunaField.getText());
                    System.out.println("Perda força [Flora - alimento]: " + lossForceFloraField.getText());
                    System.out.println("Força default [Fauna]: " + forceDefaultFaunaField.getText());
                    System.out.println("Perda de força [Fauna - movimento]: " + lossForceFaunaMoveField.getText());


                    simuladorManager.config(
                            forceDefaultFloraField.getText(),
                            gainForceFaunaField.getText(),
                            lossForceFloraField.getText(),
                            forceDefaultFaunaField.getText(),
                            lossForceFaunaMoveField.getText()
                    );
                }
                return null;
            });

            dialog.showAndWait();
            simuladorManager.CreateSimulador();
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("(*.dat)", "*.dat"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                simuladorManager.load(hFile);
            }
        });

        mnSave.setOnAction(e -> {
            simuladorManager.pause();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("(*.dat)", "*.dat"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                simuladorManager.saveEcossistema(hFile);
            }
            simuladorManager.pause();
        });

        mnExport.setOnAction(e -> {
            simuladorManager.pause();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("(*.csv)", "*.csv"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null)
                simuladorManager.export(hFile);
            simuladorManager.pause();
        });

        mnImport .setOnAction(e -> {
            simuladorManager.pause();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("(*.csv)", "*.csv"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                simuladorManager.importEcossistema(hFile);
            }
            simuladorManager.pause();
        });

        mnExit.setOnAction(e -> {
            if(simuladorManager.isSaved()){
                Platform.exit();
                return;
            }
            simuladorManager.pause();
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Deseja gurdar antes de sair?",
                    ButtonType.YES, ButtonType.NO
            );
            alert.setTitle("Sair");
            alert.setHeaderText("Guardar antes de sair?");

            ImageView exitIcon = new ImageView(ImageManager.getImage("sad.png"));
            exitIcon.setFitHeight(100);
            exitIcon.setFitWidth(100);
            alert.getDialogPane().setGraphic(exitIcon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("File save...");
                    fileChooser.setInitialDirectory(new File("."));
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("(*.dat)", "*.dat"),
                            new FileChooser.ExtensionFilter("All", "*.*")
                    );
                    File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
                    if (hFile != null) {
                        simuladorManager.saveEcossistema(hFile);
                    }
                }
                // Código para sair da aplicação
                Platform.exit();
            });
        });

        //ecossistema

        mnAddInaninamdo.setOnAction(e -> {
            simuladorManager.addInanimados();
        });
        mnAddFlora.setOnAction(e -> {
            simuladorManager.addFlora();
        });

        mnAddFauna.setOnAction(e -> {
            simuladorManager.addFauna();
        });

        mnEdit.setOnAction(e -> {
            simuladorManager.pause();
            Scene scene = this.getScene();
            if (scene != null) {
                Image cursorImage = ImageManager.getImage("edit.png");
                ImageCursor customCursor = new ImageCursor(cursorImage);
                scene.setCursor(customCursor);

                scene.setOnMousePressed(event -> {
                    double mouseX = event.getX();
                    double mouseY = event.getY();

                    if(simuladorManager.getElementoXY(mouseX,mouseY-this.getHeight()).equals("flora")){
                        Dialog<Void> dialog = new Dialog<>();
                        dialog.setTitle("Editar Flora");
                        dialog.setHeaderText("Introduza os seguintes detalhes:");

                        // Set the button types.
                        ButtonType submitButtonType = new ButtonType("Submit", ButtonType.OK.getButtonData());
                        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

                        // Create the input fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField forceDefaultFloraField = new TextField();
                        forceDefaultFloraField.setPromptText("Força default [Flora]");
                        TextField lossForceFloraField = new TextField();
                        lossForceFloraField.setPromptText("Perda força [Flora - alimento]");

                        grid.add(new Label("Força default [Flora]:"), 0, 0);
                        grid.add(forceDefaultFloraField, 1, 0);
                        grid.add(new Label("Perda força [Flora - alimento]:"), 0, 2);
                        grid.add(lossForceFloraField, 1, 2);

                        dialog.getDialogPane().setContent(grid);

                        // Request focus on the first field by default.
                        Platform.runLater(() -> forceDefaultFloraField.requestFocus());

                        // Handle the result.
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == submitButtonType) {
                                System.out.println("Força default [Flora]: " + forceDefaultFloraField.getText());
                                System.out.println("Perda força [Flora - alimento]: " + lossForceFloraField.getText());


                                simuladorManager.editFlora(
                                        mouseX,
                                        mouseY,
                                        forceDefaultFloraField.getText(),
                                        lossForceFloraField.getText()
                                );
                            }
                            return null;
                        });

                        dialog.showAndWait();
                    }
                    else if(simuladorManager.getElementoXY(mouseX,mouseY-this.getHeight()).equals("fauna")){
                        Dialog<Void> dialog = new Dialog<>();
                        dialog.setTitle("Editar Fauna");
                        dialog.setHeaderText("Introduza os seguintes detalhes:");

                        // Set the button types.
                        ButtonType submitButtonType = new ButtonType("Submit", ButtonType.OK.getButtonData());
                        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

                        // Create the input fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField gainForceFaunaField = new TextField();
                        gainForceFaunaField.setPromptText("Ganho força [Fauna - alimento]");
                        TextField forceDefaultFaunaField = new TextField();
                        forceDefaultFaunaField.setPromptText("Força default [Fauna]");
                        TextField lossForceFaunaMoveField = new TextField();
                        lossForceFaunaMoveField.setPromptText("Perda de força [Fauna - movimento]");

                        grid.add(new Label("Ganho força [Fauna - alimento]:"), 0, 1);
                        grid.add(gainForceFaunaField, 1, 1);
                        grid.add(new Label("Força default [Fauna]:"), 0, 3);
                        grid.add(forceDefaultFaunaField, 1, 3);
                        grid.add(new Label("Perda de força [Fauna - movimento]:"), 0, 4);
                        grid.add(lossForceFaunaMoveField, 1, 4);

                        dialog.getDialogPane().setContent(grid);

                        // Request focus on the first field by default.
                        Platform.runLater(() -> gainForceFaunaField.requestFocus());

                        // Handle the result.
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == submitButtonType) {
                                System.out.println("Ganho força [Fauna - alimento]: " + gainForceFaunaField.getText());
                                System.out.println("Força default [Fauna]: " + forceDefaultFaunaField.getText());
                                System.out.println("Perda de força [Fauna - movimento]: " + lossForceFaunaMoveField.getText());


                                simuladorManager.editFauna(
                                        mouseX,
                                        mouseY,
                                        gainForceFaunaField.getText(),
                                        forceDefaultFaunaField.getText(),
                                        lossForceFaunaMoveField.getText()
                                );
                            }
                            return null;
                        });

                        dialog.showAndWait();
                    }
                    else{
                        // POPUP - ELEMENTO INVALIDO OU NAO ENCONTRADO
                    }
                });


                scene.setOnMouseReleased(event -> {
                    //resetar tudo
                    scene.setOnMousePressed(null);
                    scene.setOnMouseDragged(null);
                    scene.setCursor(null);
                });
            }
            simuladorManager.pause();
        });

        mnUndo.setOnAction(e -> {
            simuladorManager.undo();
        });

        mnRedo.setOnAction(e -> {
            simuladorManager.redo();
        });

        mnDelete.setOnAction(e -> {
            Scene scene = this.getScene();
            if (scene != null) {
                Image cursorImage = ImageManager.getImage("remove.png");
                ImageCursor customCursor = new ImageCursor(cursorImage);
                scene.setCursor(customCursor);

                scene.setOnMousePressed(event -> {
                    double mouseX = event.getX();
                    double mouseY = event.getY();
                    simuladorManager.delete(mouseX, mouseY-this.getHeight());
                });

                scene.setOnMouseReleased(event -> {
                    //resetar tudo
                    scene.setOnMousePressed(null);
                    scene.setOnMouseDragged(null);
                    scene.setCursor(null);
                });
            }
        });

        mnConfig.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Editar configurações");
            dialog.setHeaderText("Introduza os seguintes detalhes:");

            // Set the button types.
            ButtonType submitButtonType = new ButtonType("Submit", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            // Create the input fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField forceDefaultFloraField = new TextField();
            forceDefaultFloraField.setPromptText("Força default [Flora]");
            TextField gainForceFaunaField = new TextField();
            gainForceFaunaField.setPromptText("Ganho força [Fauna - alimento]");
            TextField lossForceFloraField = new TextField();
            lossForceFloraField.setPromptText("Perda força [Flora - alimento]");
            TextField forceDefaultFaunaField = new TextField();
            forceDefaultFaunaField.setPromptText("Força default [Fauna]");
            TextField lossForceFaunaMoveField = new TextField();
            lossForceFaunaMoveField.setPromptText("Perda de força [Fauna - movimento]");

            grid.add(new Label("Força default [Flora]:"), 0, 0);
            grid.add(forceDefaultFloraField, 1, 0);
            grid.add(new Label("Ganho força [Fauna - alimento]:"), 0, 1);
            grid.add(gainForceFaunaField, 1, 1);
            grid.add(new Label("Perda força [Flora - alimento]:"), 0, 2);
            grid.add(lossForceFloraField, 1, 2);
            grid.add(new Label("Força default [Fauna]:"), 0, 3);
            grid.add(forceDefaultFaunaField, 1, 3);
            grid.add(new Label("Perda de força [Fauna - movimento]:"), 0, 4);
            grid.add(lossForceFaunaMoveField, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // Request focus on the first field by default.
            Platform.runLater(() -> forceDefaultFloraField.requestFocus());

            // Handle the result.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    System.out.println("Força default [Flora]: " + forceDefaultFloraField.getText());
                    System.out.println("Ganho força [Fauna - alimento]: " + gainForceFaunaField.getText());
                    System.out.println("Perda força [Flora - alimento]: " + lossForceFloraField.getText());
                    System.out.println("Força default [Fauna]: " + forceDefaultFaunaField.getText());
                    System.out.println("Perda de força [Fauna - movimento]: " + lossForceFaunaMoveField.getText());


                    simuladorManager.config(
                            forceDefaultFloraField.getText(),
                            gainForceFaunaField.getText(),
                            lossForceFloraField.getText(),
                            forceDefaultFaunaField.getText(),
                            lossForceFaunaMoveField.getText()
                    );
                }
                return null;
            });

            dialog.showAndWait();
        });


        //Simulacao

        mnPause.setOnAction(e -> {
         if (simuladorManager.pause()){
            mnPause.setText("Retomar");
            }else{
            mnPause.setText("Pausar");
             }
        });

        mnSaveSnap.setOnAction(e -> {
            simuladorManager.saveSnap();
        });

        mnRestoreSanap.setOnAction(e -> {
            simuladorManager.restoreSnap();
        });


        //Evento

        mnAddSol.setOnAction(e -> {
            simuladorManager.sun();
        });

        mnApplyHerb.setOnAction(e -> {
            Scene scene = this.getScene();
            if (scene != null) {
                Image cursorImage = ImageManager.getImage("herbicida.png");
                ImageCursor customCursor = new ImageCursor(cursorImage);
                scene.setCursor(customCursor);

                scene.setOnMousePressed(event -> {
                    double mouseX = event.getX();
                    double mouseY = event.getY();
                    simuladorManager.herbicida(mouseX, mouseY-this.getHeight());
                });

                scene.setOnMouseDragged(event -> {
                    double mouseX = event.getX();
                    double mouseY = event.getY();
                    simuladorManager.herbicida(mouseX, mouseY-this.getHeight());
                });

                scene.setOnMouseReleased(event -> {
                    //resetar tudo
                    scene.setOnMousePressed(null);
                    scene.setOnMouseDragged(null);
                    scene.setCursor(null);
                });
            }
        });

        mnInForca.setOnAction(e -> {
            Scene scene = this.getScene();
            if (scene != null) {
                Image cursorImage = ImageManager.getImage("barbell.png");
                ImageCursor customCursor = new ImageCursor(cursorImage);
                scene.setCursor(customCursor);

                scene.setOnMouseClicked(event -> {
                    double mouseX = event.getX();
                    double mouseY = event.getY();
                    simuladorManager.strengh(mouseX, mouseY-this.getHeight());

                    //resetar tudo
                    scene.setOnMouseClicked(null);
                    scene.setCursor(null);

                });
            }
        });
    }

    private void update() {

    }
}
