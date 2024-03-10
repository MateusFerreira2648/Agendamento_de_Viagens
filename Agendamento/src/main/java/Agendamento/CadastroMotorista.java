package Agendamento;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;


public class CadastroMotorista {

    private Set<String> CNHsCadastradas = new HashSet<>();

    public void mostrarTelaCadastroMotorista() {
        Stage stage = new Stage();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        Label nomeLabel = new Label("Nome:");
        TextField nomeField = new TextField();

        Label CNHLabel = new Label("CNH:");
        TextField CNHField = new TextField();
        
        Label ENDLabel = new Label("Endereço:");
        TextField ENDField = new TextField();

        Label TelefoneLabel = new Label("Telefone:");
        TextField TelefoneField = new TextField();
        
        grid.add(nomeLabel, 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(CNHLabel, 0, 1);
        grid.add(CNHField, 1, 1);
        grid.add(ENDLabel, 0, 2);
        grid.add(ENDField, 1, 2);
        grid.add(TelefoneLabel, 0, 3);
        grid.add(TelefoneField, 1, 3);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setOnAction(event -> {
            if (validarCampos(nomeField.getText(), CNHField.getText(), ENDField.getText(), TelefoneField.getText())) {
                cadastroMotorista(nomeField.getText(), CNHField.getText(), ENDField.getText(), TelefoneField.getText());
                CNHsCadastradas.add(CNHField.getText());
                stage.close();
            }
        });

        grid.add(cadastrarButton, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Cadastro de Motorista");
        stage.show();
    }
    
    // VALIDAR OS CAMPOS PREENCHIDOS

    private boolean validarCampos(String nome, String CNH,  String END,String Telefone) {
        if (nome.isEmpty() || CNH.isEmpty() || Telefone.isEmpty()) {
            exibirAlerta("Erro", "Todos os campos devem ser preenchidos.");
            return false;
        }

        if (!nome.matches("[a-z A-Z]+")) {
            exibirAlerta("Erro", "O campo 'Nome' deve conter apenas letras.");
            return false;
        }

        if (!CNH.matches("[0-9]+")) {
            exibirAlerta("Erro", "O campo 'CNH' deve conter apenas números.");
            return false;
        }

        if (CNHsCadastradas.contains(CNH)) {
            exibirAlerta("Erro", "CNH já cadastrada.");
            return false;
        }

        return true;
    }
    
    // SAIDA DAS INFORMAÇÕES NO CONSOLE
    
    private void cadastroMotorista(String nome, String CNH, String END, String Telefone) {
    	
        System.out.println("Motorista cadastrado:");
        System.out.println("Nome: " + nome);
        System.out.println("CNH: " + CNH);
        System.out.println("Endereço: " + END);
        System.out.println("Telefone: " + Telefone);
    
    }
     
    // EXIBE MENSAGEM DE ERRO NA VALIDAÇÃO
    
    private void exibirAlerta(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
