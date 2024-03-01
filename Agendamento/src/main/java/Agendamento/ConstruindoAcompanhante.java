package Agendamento;
import java.text.DateFormatSymbols;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ConstruindoAcompanhante extends Application {

    private Map<LocalDate, Viagem> viagens;

    @Override
    public void start(Stage primaryStage) throws Exception {
        viagens = new HashMap<>();

        BorderPane root = new BorderPane();

        Button cadastrarButton = new Button("Cadastrar Viagem");
        Button consultarButton = new Button("Consultar Viagens");

        VBox optionsBox = new VBox(10, cadastrarButton, consultarButton);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setStyle("-fx-alignment: center;");

        cadastrarButton.setOnAction(event -> {
            mostrarTelaCadastro();
        });

        consultarButton.setOnAction(event -> {
            // Implemente a funcionalidade de consulta aqui
            System.out.println("Consulta de viagens");
        });

        root.setCenter(optionsBox);

        Scene scene = new Scene(root, 800, 600); // Tamanho inicial da janela
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Adicionando um estilo CSS
        primaryStage.setScene(scene);
        primaryStage.setTitle("Opções");
        primaryStage.setMaximized(true); // Janela maximizada por padrão
        primaryStage.show();
    }

    private void mostrarTelaCadastro() {
        Stage stage = new Stage();

        BorderPane root = new BorderPane();

        DatePicker datePicker = new DatePicker();
        datePicker.setEditable(false);
        datePicker.setFocusTraversable(false);
        datePicker.setPromptText("Selecione a data");
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now())); // Desabilita datas anteriores à atual
            }
        });

        CheckBox acompanhanteCheckBox = new CheckBox("Paciente terá acompanhante");

        // Campos do acompanhante
        TextField nomeAcompanhanteField = new TextField();
        nomeAcompanhanteField.setPromptText("Nome do Acompanhante");
        nomeAcompanhanteField.setVisible(false);

        TextField rgAcompanhanteField = new TextField();
        rgAcompanhanteField.setPromptText("RG do Acompanhante");
        rgAcompanhanteField.setVisible(false);

        TextField cartaoSusAcompanhanteField = new TextField();
        cartaoSusAcompanhanteField.setPromptText("Cartão SUS do Acompanhante");
        cartaoSusAcompanhanteField.setVisible(false);
        cartaoSusAcompanhanteField.setTextFormatter(new TextFormatter<>(aceitarApenasNumeros()));

        acompanhanteCheckBox.setOnAction(event -> {
            boolean acompanhanteSelecionado = acompanhanteCheckBox.isSelected();
            nomeAcompanhanteField.setVisible(acompanhanteSelecionado);
            rgAcompanhanteField.setVisible(acompanhanteSelecionado);
            cartaoSusAcompanhanteField.setVisible(acompanhanteSelecionado);
        });
        
        TextField cartaoSusField = new TextField();
        cartaoSusField.setPromptText("Número do Cartão SUS");
        cartaoSusField.setTextFormatter(new TextFormatter<>(aceitarApenasNumeros()));

        TextField nomePacienteField = new TextField();
        nomePacienteField.setPromptText("Nome do Paciente");

        TextField rgPacienteField = new TextField();
        rgPacienteField.setPromptText("RG do Paciente");

        TextField destinoField = new TextField();
        destinoField.setPromptText("Destino");

        TextField enderecoDestinoField = new TextField();
        enderecoDestinoField.setPromptText("Endereço do Destino");

        ChoiceBox<String> pontoPacienteChoiceBox = new ChoiceBox<>();
        pontoPacienteChoiceBox.getItems().addAll("PSF Bom Jesus", "Antiga Distribuidora Nabi Miguel", "Complexo de Saúde", "Casa do passageiro", "Outros");

        TextField enderecoPacienteField = new TextField();
        enderecoPacienteField.setPromptText("Endereço do Passageiro");
        enderecoPacienteField.setVisible(false);

        Label enderecoPacienteLabel = new Label("Endereço do Passageiro");
        enderecoPacienteLabel.setVisible(false);

        pontoPacienteChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("Casa do passageiro")) {
                enderecoPacienteField.setVisible(true);
                enderecoPacienteLabel.setVisible(true);
            } else {
                enderecoPacienteField.setVisible(false);
                enderecoPacienteLabel.setVisible(false);
            }
        });

        TextField observacoesField = new TextField();
        observacoesField.setPromptText("Observações");

        TextField motoristaField = new TextField();
        motoristaField.setPromptText("Motorista Designado");

        Button cadastrarButton = new Button("Cadastrar Viagem");
        cadastrarButton.setOnAction(event -> {
            LocalDate dataSelecionada = datePicker.getValue();
            if (dataSelecionada == null) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Por favor, selecione uma data.");
                return;
            }

            Viagem viagem = new Viagem(destinoField.getText(), pontoPacienteChoiceBox.getValue(),
                    cartaoSusField.getText(), nomePacienteField.getText(), rgPacienteField.getText(),
                    observacoesField.getText(), motoristaField.getText(), enderecoPacienteField.getText(),
                    enderecoDestinoField.getText());

            if (acompanhanteCheckBox.isSelected()) {
                viagem.setAcompanhante(nomeAcompanhanteField.getText(), rgAcompanhanteField.getText(), cartaoSusAcompanhanteField.getText());
            }
            
            mostrarResumoCadastro(viagem, dataSelecionada);
         // Limpa os campos após o cadastro
            datePicker.setValue(null);
            cartaoSusField.clear();
            nomePacienteField.clear();
            rgPacienteField.clear();
            destinoField.clear();
            enderecoDestinoField.clear();
            pontoPacienteChoiceBox.getSelectionModel().clearSelection();
            enderecoPacienteField.clear();
            observacoesField.clear();
            motoristaField.clear();
            
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        grid.add(new Label("Data:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Cartão SUS:"), 0, 1);
        grid.add(cartaoSusField, 1, 1);
        grid.add(new Label("Nome do Paciente:"), 0, 2);
        grid.add(nomePacienteField, 1, 2);
        grid.add(new Label("RG do Paciente:"), 0, 3);
        grid.add(rgPacienteField, 1, 3);
        grid.add(new Label("Destino:"), 0, 4);
        grid.add(destinoField, 1, 4);
        grid.add(new Label("Endereço do Destino:"), 0, 5);
        grid.add(enderecoDestinoField, 1, 5);
        grid.add(new Label("Ponto do Paciente:"), 0, 6);
        grid.add(pontoPacienteChoiceBox, 1, 6);
        grid.add(enderecoPacienteLabel, 0, 7);
        grid.add(enderecoPacienteField, 1, 7);
        grid.add(new Label("Observações:"), 0, 8);
        grid.add(observacoesField, 1, 8);
        grid.add(new Label("Motorista Designado:"), 0, 9);
        grid.add(motoristaField, 1, 9);
        grid.add(acompanhanteCheckBox, 0, 10);
        grid.add(new Label("Nome do Acompanhante:"), 0, 11);
        grid.add(nomeAcompanhanteField, 1, 11);
        grid.add(new Label("RG do Acompanhante:"), 0, 12);
        grid.add(rgAcompanhanteField, 1, 12);
        grid.add(new Label("Cartão SUS do Acompanhante:"), 0, 13);
        grid.add(cartaoSusAcompanhanteField, 1, 13);

        root.setCenter(grid);

        VBox bottomBox = new VBox(10, cadastrarButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 800, 600); // Tamanho inicial da janela
        stage.setScene(scene);
        stage.setTitle("Cadastro de Viagens");
        stage.show();
    }
    
    private void mostrarResumoCadastro(Viagem viagem, LocalDate dataSelecionada) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setText("Resumo da Viagem:\n\n" +
                "Data: " + formatarData(dataSelecionada) + "\n" +
                "Cartão SUS: " + viagem.getCartaoSus() + "\n" +
                "Nome do Paciente: " + viagem.getNomePaciente() + "\n" +
                "RG do Paciente: " + viagem.getRgPaciente() + "\n" +
                "Destino: " + viagem.getDestino() + "\n" +
                "Endereço do Destino: " + viagem.getEnderecoDestino() + "\n" +
                "Ponto do Paciente: " + viagem.getPontoPaciente() + "\n" +
                "Endereço do Passageiro: " + viagem.getEnderecoPaciente() + "\n" +
                "Observações: " + viagem.getObservacoes() + "\n" +
                "Motorista Designado: " + viagem.getMotorista());
        
        if (viagem.temAcompanhante()) {
            textArea.appendText("\n\nAcompanhante:\n" +
                    "Nome do Acompanhante: " + viagem.getNomeAcompanhante() + "\n" +
                    "RG do Acompanhante: " + viagem.getRgAcompanhante() + "\n" +
                    "Cartão SUS do Acompanhante: " + viagem.getCartaoSusAcompanhante());
        }

        
        // AQUIIIIIIIIIIIIIIIIIII
        
        
        
        Button confirmarButton = new Button("Confirmar Cadastro");
        confirmarButton.setOnAction(event -> {
            viagens.put(dataSelecionada, viagem);
            boolean cadastradoComSucesso = cadastrarViagem(dataSelecionada, viagem.getDestino(), viagem.getPontoPaciente(),
                    viagem.getCartaoSus(), viagem.getNomePaciente(), viagem.getRgPaciente(), viagem.getObservacoes(), viagem.getMotorista(), viagem.getEnderecoPaciente(), viagem.getEnderecoDestino());
            if (cadastradoComSucesso) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Viagem cadastrada para " + formatarData(dataSelecionada));
                try {
                    exportarParaExcel();
                } catch (Exception e) {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao exportar para o Excel.");
                    e.printStackTrace();
                }
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar a viagem.");
            }
            stage.close();
        });

        Button alterarButton = new Button("Alterar Informações");
        alterarButton.setOnAction(event -> {
            stage.close();
        });

        HBox buttonBox = new HBox(10, confirmarButton, alterarButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10, textArea, buttonBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        root.setCenter(vBox);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Resumo do Cadastro");
        stage.showAndWait();
    }
    private boolean cadastrarViagem(LocalDate dataSelecionada, String destino, String pontoPaciente, String cartaoSus,
                                     String nomePaciente, String rgPaciente, String observacoes, String motorista, String enderecoPaciente, String enderecoDestino) {
        if (cartaoSus == null || cartaoSus.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Por favor, preencha o número do Cartão SUS.");
            return false;
        }
        viagens.put(dataSelecionada, new Viagem(destino, pontoPaciente, cartaoSus, nomePaciente, rgPaciente, observacoes, motorista, enderecoPaciente, enderecoDestino));
        return true;
    }

    private String formatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    private void exportarParaExcel() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        for (Map.Entry<LocalDate, Viagem> entry : viagens.entrySet()) {
            LocalDate data = entry.getKey();
            Viagem viagem = entry.getValue();

            Sheet sheet = workbook.getSheet(getNomeMes(data.getMonthValue()));
            if (sheet == null) {
                sheet = workbook.createSheet(getNomeMes(data.getMonthValue()));
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Data");
                headerRow.createCell(1).setCellValue("Cartão SUS");
                headerRow.createCell(2).setCellValue("Nome do Paciente");
                headerRow.createCell(3).setCellValue("RG do Paciente");
                headerRow.createCell(4).setCellValue("Destino");
                headerRow.createCell(5).setCellValue("Endereço do Destino");
                headerRow.createCell(6).setCellValue("Ponto do Paciente");
                headerRow.createCell(7).setCellValue("Endereço do Passageiro");
                headerRow.createCell(8).setCellValue("Observações");
                headerRow.createCell(9).setCellValue("Motorista");
            }

            int rowNum = sheet.getLastRowNum() +1 ;
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(formatarData(data));
            row.createCell(1).setCellValue(viagem.getCartaoSus());
            row.createCell(2).setCellValue(viagem.getNomePaciente());
            row.createCell(3).setCellValue(viagem.getRgPaciente());
            row.createCell(4).setCellValue(viagem.getDestino());
            row.createCell(5).setCellValue(viagem.getEnderecoDestino());
            row.createCell(6).setCellValue(viagem.getPontoPaciente());
            row.createCell(7).setCellValue(viagem.getEnderecoPaciente());
            row.createCell(8).setCellValue(viagem.getObservacoes());
            row.createCell(9).setCellValue(viagem.getMotorista());
        }

        FileOutputStream fileOut = new FileOutputStream("viagens.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private String getNomeMes(int numeroMes) {
        return new DateFormatSymbols(new Locale("pt", "BR")).getMonths()[numeroMes - 1];
    }

    public static void main(String[] args) {
        launch(args);
    }

    private UnaryOperator<TextFormatter.Change> aceitarApenasNumeros() {
        return change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
    }
}

class Viagem {
    private String destino;
    private String pontoPaciente;
    private String enderecoPaciente;
    private String enderecoDestino;
    private String cartaoSus;
    private String nomePaciente;
    private String rgPaciente;
    private String observacoes;
    private String motorista;
    private boolean acompanhante;
    private String nomeAcompanhante;
    private String rgAcompanhante;
    private String cartaoSusAcompanhante;

    public Viagem(String destino, String pontoPaciente, String cartaoSus, String nomePaciente, String rgPaciente, String observacoes, String motorista, String enderecoPaciente, String enderecoDestino) {
        this.destino = destino;
        this.pontoPaciente = pontoPaciente;
        this.cartaoSus = cartaoSus;
        this.nomePaciente = nomePaciente;
        this.rgPaciente = rgPaciente;
        this.observacoes = observacoes;
        this.motorista = motorista;
        this.enderecoPaciente = enderecoPaciente;
        this.enderecoDestino = enderecoDestino;
        this.acompanhante = false;
    }

    public void setAcompanhante(String nomeAcompanhante, String rgAcompanhante, String cartaoSusAcompanhante) {
        this.acompanhante = true;
        this.nomeAcompanhante = nomeAcompanhante;
        this.rgAcompanhante = rgAcompanhante;
        this.cartaoSusAcompanhante = cartaoSusAcompanhante;
    }

    public boolean temAcompanhante() {
        return acompanhante;
    }

    public String getDestino() {
        return destino;
    }

    public String getPontoPaciente() {
        return pontoPaciente;
    }

    public String getEnderecoPaciente() {
        return enderecoPaciente;
    }

    public String getEnderecoDestino() {
        return enderecoDestino;
    }

    public String getCartaoSus() {
        return cartaoSus;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getRgPaciente() {
        return rgPaciente;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getMotorista() {
        return motorista;
    }

    public String getNomeAcompanhante() {
        return nomeAcompanhante;
    }

    public String getRgAcompanhante() {
        return rgAcompanhante;
    }

    public String getCartaoSusAcompanhante() {
        return cartaoSusAcompanhante;
    }
}