package empresa;

import java.sql.*;
import java.util.Scanner;

import util.Conexao;


public class Principal {

    // Método principal que inicia o programa e gerencia as opções do menu.
	
    public static void main(String[] args) throws Exception {
        // Abrindo o scanner
        Scanner sc = new Scanner(System.in);
        
        // Iniciando a conexão com o banco de dados
        Connection con = Conexao.conectar();

        // Variável para opção escolhida pelo usuário
        int escolhaDeAcao;

        // Loop do menu principal
        do {
            menu(); // Exibe o menu de ações
            escolhaDeAcao = sc.nextInt(); // Lê a opção
            switch (escolhaDeAcao) {
                case 1: // Inserção de dados
                    opcaoClasse(); // Menu de escolha de entidade
                    int opcaoInsert = sc.nextInt();

                    if (opcaoInsert == 1) { // Inserir Projeto
                        System.out.print("Nome do projeto: ");
                        sc.nextLine(); // Limpa o buffer
                        String nome = sc.nextLine();
                        System.out.print("Descrição: ");
                        String descricao = sc.nextLine();
                        System.out.print("ID do funcionário: ");
                        int idFuncionario = sc.nextInt();
                        sc.nextLine(); // Limpa o buffer

                        // Verifica se o funcionário existe
                        if (existeFuncionario(con, idFuncionario)) {
                            // Insere o projeto
                            PreparedStatement stm = con.prepareStatement(
                                "INSERT INTO projeto (nome, descricao, id_funcionario) VALUES (?, ?, ?)");
                            stm.setString(1, nome);
                            stm.setString(2, descricao);
                            stm.setInt(3, idFuncionario);
                            stm.executeUpdate();
                            System.out.println("Projeto inserido com sucesso!");
                        } else {
                            System.out.println("Erro: funcionário não encontrado.");
                        }

                    } else if (opcaoInsert == 2) { // Inserir Pessoa
                        System.out.print("Nome: ");
                        sc.nextLine(); // Limpa o buffer
                        String nome = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        PreparedStatement stm = con.prepareStatement(
                            "INSERT INTO pessoa (nome, email) VALUES (?, ?)");
                        stm.setString(1, nome);
                        stm.setString(2, email);
                        stm.executeUpdate();
                        System.out.println("Pessoa inserida com sucesso!");

                    } else if (opcaoInsert == 3) { // Inserir Funcionário
                        System.out.print("ID da pessoa: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // Limpa o buffer
                        System.out.print("Matrícula (Fxxx): ");
                        String matricula = sc.nextLine();
                        System.out.print("Departamento: ");
                        String departamento = sc.nextLine();

                        // Verifica se a pessoa existe
                        if (existePessoa(con, id)) {
                            PreparedStatement stm = con.prepareStatement(
                                "INSERT INTO funcionario (id, matricula, departamento) VALUES (?, ?, ?)");
                            stm.setInt(1, id);
                            stm.setString(2, matricula);
                            stm.setString(3, departamento);
                            stm.executeUpdate();
                            System.out.println("Funcionário inserido com sucesso!");
                        } else {
                            System.out.println("Erro: pessoa não existe.");
                        }
                    }
                    break;

                case 2: // Exclusão de dados
                    opcaoClasse();
                    int opcaoDelete = sc.nextInt();
                    sc.nextLine();

                    if (opcaoDelete == 1) { // Excluir Projeto
                        System.out.print("ID do projeto a excluir: ");
                        int idProjeto = sc.nextInt();
                        sc.nextLine();
                        PreparedStatement stmt = con.prepareStatement("DELETE FROM projeto WHERE id = ?");
                        stmt.setInt(1, idProjeto);
                        int res = stmt.executeUpdate();
                        if (res > 0) System.out.println("Projeto excluído com sucesso!");
                        else System.out.println("Projeto não encontrado.");
                    }

                    else if (opcaoDelete == 2) { // Excluir Pessoa
                        System.out.print("ID da pessoa a excluir: ");
                        int idPessoa = sc.nextInt();
                        sc.nextLine();

                        // Verifica se a pessoa é um funcionário antes de excluir
                        if (existeFuncionario(con, idPessoa)) {
                            System.out.println("Erro: esta pessoa é um funcionário. Exclua o funcionário antes.");
                        } else {
                            PreparedStatement stmt = con.prepareStatement("DELETE FROM pessoa WHERE id = ?");
                            stmt.setInt(1, idPessoa);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Pessoa excluída com sucesso!");
                            else System.out.println("Pessoa não encontrada.");
                        }
                    }

                    else if (opcaoDelete == 3) { // Excluir Funcionário
                        System.out.print("ID do funcionário a excluir: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        // Verifica se o funcionário está vinculado a projetos
                        if (funcionarioTemProjetos(con, id)) {
                            System.out.println("Erro: funcionário vinculado a projeto.");
                        } else {
                            PreparedStatement stmt = con.prepareStatement("DELETE FROM funcionario WHERE id = ?");
                            stmt.setInt(1, id);
                            int result = stmt.executeUpdate();
                            if (result > 0) System.out.println("Funcionário excluído com sucesso!");
                            else System.out.println("Funcionário não encontrado.");
                        }
                    }
                    break;

                case 3: // Atualização de dados
                    opcaoClasse();
                    int opcaoUpdate = sc.nextInt();
                    sc.nextLine();

                    if (opcaoUpdate == 1) { // Atualizar Projeto
                        System.out.print("ID do projeto: ");
                        int idProjeto = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Novo nome: ");
                        String novoNome = sc.nextLine();
                        System.out.print("Nova descrição: ");
                        String novaDesc = sc.nextLine();
                        System.out.print("Novo ID do funcionário responsável: ");
                        int novoIdFunc = sc.nextInt();
                        sc.nextLine();

                        if (existeFuncionario(con, novoIdFunc)) {
                            PreparedStatement stmt = con.prepareStatement(
                                "UPDATE projeto SET nome = ?, descricao = ?, id_funcionario = ? WHERE id = ?");
                            stmt.setString(1, novoNome);
                            stmt.setString(2, novaDesc);
                            stmt.setInt(3, novoIdFunc);
                            stmt.setInt(4, idProjeto);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Projeto atualizado com sucesso!");
                            else System.out.println("Projeto não encontrado.");
                        } else {
                            System.out.println("Erro: funcionário informado não existe.");
                        }

                    } else if (opcaoUpdate == 2) { // Atualizar Pessoa
                        System.out.print("ID da pessoa: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Novo nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Novo email: ");
                        String email = sc.nextLine();

                        PreparedStatement stmt = con.prepareStatement(
                            "UPDATE pessoa SET nome = ?, email = ? WHERE id = ?");
                        stmt.setString(1, nome);
                        stmt.setString(2, email);
                        stmt.setInt(3, id);
                        int res = stmt.executeUpdate();
                        if (res > 0) System.out.println("Pessoa atualizada com sucesso!");
                        else System.out.println("Pessoa não encontrada.");

                    } else if (opcaoUpdate == 3) { // Atualizar Funcionário
                        System.out.print("ID do funcionário: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Nova matrícula (Fxxx): ");
                        String novaMat = sc.nextLine();
                        System.out.print("Novo departamento: ");
                        String novoDepto = sc.nextLine();

                        if (existeFuncionario(con, id)) {
                            PreparedStatement stmt = con.prepareStatement(
                                "UPDATE funcionario SET matricula = ?, departamento = ? WHERE id = ?");
                            stmt.setString(1, novaMat);
                            stmt.setString(2, novoDepto);
                            stmt.setInt(3, id);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Funcionário atualizado com sucesso!");
                            else System.out.println("Funcionário não encontrado.");
                        } else {
                            System.out.println("Erro: funcionário não encontrado.");
                        }
                    }
                    break;

                case 4: // Consulta de dados
                    opcaoClasse();
                    int opcaoSelect = sc.nextInt();
                    sc.nextLine();

                    if (opcaoSelect == 1) { // Consultar Projetos
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM projeto");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Nome: " + rs.getString("nome") +
                                    ", Funcionário: " + rs.getInt("id_funcionario"));
                        }
                    } else if (opcaoSelect == 2) { // Consultar Pessoas
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM pessoa");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Nome: " + rs.getString("nome") +
                                    ", Email: " + rs.getString("email"));
                        }
                    } else if (opcaoSelect == 3) { // Consultar Funcionários
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM funcionario");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Matrícula: " + rs.getString("matricula") +
                                    ", Departamento: " + rs.getString("departamento"));
                        }
                    }
                    break;

                case 5: // Encerrar
                    System.out.println("Encerrando sistema...");
                    break;

                default: // Opção inválida
                    System.out.println("Opção inválida.");
            }

        } while (escolhaDeAcao != 5); // Repetição até a escolha de sair

        // Fecha recursos
        sc.close();
        con.close();
    }

    // Exibe o menu principal de ações disponíveis.
    
    public static void menu() {
        System.out.println("\nSelecione a ação:");
        System.out.println("1 - Inserir");
        System.out.println("2 - Deletar");
        System.out.println("3 - Atualizar");
        System.out.println("4 - Consultar");
        System.out.println("5 - Sair");
    }

    // Exibe o menu de classes disponíveis para manipulação.
     
    public static void opcaoClasse() {
        System.out.println("Escolha a classe:");
        System.out.println("1 - Projeto");
        System.out.println("2 - Pessoa");
        System.out.println("3 - Funcionário");
    }

    // Verifica se uma pessoa com o ID especificado existe.
    
    public static boolean existePessoa(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT id FROM pessoa WHERE id = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }

    // Verifica se um funcionário com o ID especificado existe.
    
    public static boolean existeFuncionario(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT id FROM funcionario WHERE id = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }

    // Verifica se um funcionário está vinculado a algum projeto.
    
     
    public static boolean funcionarioTemProjetos(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM projeto WHERE id_funcionario = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }
}
