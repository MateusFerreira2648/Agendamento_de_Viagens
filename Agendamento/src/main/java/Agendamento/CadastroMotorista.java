package Agendamento;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CadastroMotorista {

    private Set<String> matriculasCadastradas = new HashSet<>();

    public void mostrarTelaCadastroMotorista() {
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
            if (validarCampos(nomeField.getText(), matriculaField.getText(), dataNascimentoPicker.getValue())) {
                cadastroMotorista(nomeField.getText(), matriculaField.getText(), dataNascimentoPicker.getValue());
                matriculasCadastradas.add(matriculaField.getText());
                stage.close();
            }
        });

        grid.add(cadastrarButton, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Cadastro de Motorista");
        stage.show();
    }

    private boolean validarCampos(String nome, String matricula, LocalDate dataNascimento) {
        if (nome.isEmpty() || matricula.isEmpty() || dataNascimento == null) {
            exibirAlerta("Erro", "Todos os campos devem ser preenchidos.");
            return false;
        }

        if (!nome.matches("[a-zA-Z]+")) {
            exibirAlerta("Erro", "O campo 'Nome' deve conter apenas letras.");
            return false;
        }

        if (!matricula.matches("[0-9]+")) {
            exibirAlerta("Erro", "O campo 'Matrícula' deve conter apenas números.");
            return false;
        }

        if (matriculasCadastradas.contains(matricula)) {
            exibirAlerta("Erro", "Matrícula já cadastrada.");
            return false;
        }

        return true;
    }

    private void cadastroMotorista(String nome, String matricula, LocalDate dataNascimento) {
        System.out.println("Motorista cadastrado:");
        System.out.println("Nome: " + nome);
        System.out.println("Matrícula: " + matricula);
        System.out.println("Data de Nascimento: " + dataNascimento);
    }

    private void exibirAlerta(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
