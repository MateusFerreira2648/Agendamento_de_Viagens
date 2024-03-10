package Agendamento;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;


public class CadastroMotorista extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Button cadastrarMotoristaButton = new Button("Cadastrar Motorista");

        cadastrarMotoristaButton.setOnAction(event -> mostrarTelaCadastroMotorista());

        root.setCenter(cadastrarMotoristaButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cadastro de Motoristas");
        primaryStage.show();
    }

    private void mostrarTelaCadastroMotorista() {
        Stage stage = new Stage();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        Label nomeLabel = new Label("Nome:");
        TextField nomeField = new TextField();

        Label matriculaLabel = new Label("Matrícula:");
        TextField matriculaField = new TextField();

        Label dataNascimentoLabel = new Label("Data de Nascimento:");
        DatePicker dataNascimentoPicker = new DatePicker();

        grid.add(nomeLabel, 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(matriculaLabel, 0, 1);
        grid.add(matriculaField, 1, 1);
        grid.add(dataNascimentoLabel, 0, 2);
        grid.add(dataNascimentoPicker, 1, 2);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setOnAction(event -> {
            cadastroMotorista(nomeField.getText(), matriculaField.getText(), dataNascimentoPicker.getValue());
            stage.close();
        });

        grid.add(cadastrarButton, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Cadastro de Motorista");
        stage.show();
    }

    private void cadastroMotorista(String nome, String matricula, LocalDate dataNascimento) {
        System.out.println("Motorista cadastrado:");
        System.out.println("Nome: " + nome);
        System.out.println("Matrícula: " + matricula);
        System.out.println("Data de Nascimento: " + dataNascimento);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
