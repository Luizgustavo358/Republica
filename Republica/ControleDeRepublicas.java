/**
* OBS : Falta implementar as ArvoresBMais e fazer a compressao de dados.
*
* Trabalho: Controle de Republicas.
*
* Grupo:
*        Isabelle Langkammer
*        Izabela Borges
*        Luiz Gustavo Bragança dos Santos
*        Mateus Roscoe
*/

import java.io.*;
import java.util.Scanner;
import java.util.*;

public class ControleDeRepublicas
{
  // definir dados
  private static Scanner console = new Scanner(System.in);

  // Arquivos de objetos.
  private static ArquivoIndexado<Republica> arqRepublica;
  private static ArquivoIndexado<Usuario> arqUsuarios;
  private static ArquivoIndexado<Tarefas> arqTarefas;
  private static ArquivoIndexado<Gastos> arqGastos;

  // Arvores de relacoes.
  private static ArvoreBMais idxRepublicaUsuario;
  private static ArvoreBMais idxUsuarioRepublica;
  private static ArvoreBMais idxUsuarioTarefas;
  private static ArvoreBMais idxTarefasUsuario;
  private static ArvoreBMais idxRepublicaGastos;
  private static ArvoreBMais idxGastosRepublica;
  private static ArvoreBMais idxRepublicaTarefas;
  private static ArvoreBMais idxTarefasRepublica;

  public static void main(String[] args)
  {
    try
    {
      arqRepublica  = new ArquivoIndexado<>(Republica.class, "Republicas.db", "Republicas1.idx", "Republicas2.idx");
      arqUsuarios = new ArquivoIndexado<>(Usuario.class  , "Usuarios.db"  , "Usuarios1.idx"  , "Usuarios2.idx"  );
      arqTarefas  = new ArquivoIndexado<>(Tarefas.class  , "Tarefas.db"   , "Tarefas1.idx"   , "Tarefas2.idx"   );
      arqGastos = new ArquivoIndexado<>(Gastos.class   , "Gastos.db"    , "Gastos1.idx"    , "Gastos2.idx"    );

      idxRepublicaUsuario = new ArvoreBMais (10, "republica_usuario.idx");
      idxUsuarioRepublica = new ArvoreBMais (10, "usuario_republica.idx");

      idxRepublicaGastos = new ArvoreBMais (10, "republica_gastos.idx");

      idxRepublicaTarefas = new ArvoreBMais (10, "republica_tarefas.idx");

      idxUsuarioTarefas = new ArvoreBMais (10, "usuario_tarefas.idx");
      idxTarefasUsuario = new ArvoreBMais (10, "tarefas_usuario.idx");

      // menu
      int opcao;

      do
      {
        System.out.println("\n\nCADASTRO DE REPUBLICAS\n");
        System.out.println("-----------------------------\n");
        System.out.println("Republica               USUÁRIOS               TAREFAS");
        System.out.println("10 - Listar                20 - Listar                30 - Listar");
        System.out.println("11 - Incluir              21 - Incluir              31 - Incluir");
        System.out.println("12 - Alterar             22 - Alterar            32 - Alterar");
        System.out.println("13 - Excluir             23 - Excluir             33 - Excluir");
        System.out.println("14 - Buscar por código     24 - Buscar por código     34 - Buscar por código");
        System.out.println("15 - Buscar por nome     25 - Buscar por nome       35 - Buscar por nome");
        System.out.println("\n41 - Lista de usuarios de uma republica");
        System.out.println("42 - Lista de republicas de um usuario");

        System.out.println("43 - Lista de usuarios de uma tarefa");
        System.out.println("44 - Lista de tarefas de um usuário");

        System.out.println("45 - Lista de gastos de uma republica");

        System.out.println("46 - Lista de tarefas de uma republica");

        System.out.println("\n98 - Povoar DB (republica)");
        System.out.println("\n99 - Reorganizar arquivos");
        System.out.println("0 - Sair");
        System.out.print("\nOpcao: ");
        opcao = Integer.valueOf(console.nextLine());

        switch(opcao)
        {
          case 10: listarRepublica( ); break;
          case 11: incluirRepublica( ); break;
          case 12: alterarRepublica( ); break;
          case 13: excluirRepublica( ); break;
          case 14: buscarRepublicaCodigo( ); break;
          case 15: buscarRepublicaNome( ); break;

          case 20: listarUsuarios( ); break;
          case 21: incluirUsuario( ); break;
          case 22: alterarUsuario( ); break;
          case 23: excluirUsuario( ); break;
          case 24: buscarUsuarioCodigo( ); break;
          case 25: buscarUsuarioNome( ); break;

          case 30: listarTarefas( ); break;
          case 31: incluirTarefa( ); break;
          case 32: alterarTarefa( ); break;
          case 33: excluirTarefa( ); break;
          case 34: buscarTarefaCodigo( ); break;
          case 35: buscarTarefaDescricao( ); break;

          case 41: listarUsuariosRepublica();break;
          case 42: listarRepublicasUsuario();break;
          case 43: listarUsuariosTarefa();break;
          case 44: listarTarefasUsuario();break;
          case 45: listarGastosRepublica();break;
          case 46: listarTarefasRepublica();break;

          case 98: povoar( ); break;
          case 99: reorganizar( ); break;
          case 0: break;
          default: System.out.println("Opção inválida");
        }// end switch
      } while(opcao != 0);
    } catch(Exception e) {
      e.printStackTrace();
    }// end try catch
  }// end main

  // Republicas
  public static void listarRepublica( ) throws Exception
  {
    Object[] Republicas = arqRepublica.listar( );

    for(int i = 0; i < Republicas.length; i++)
    {
      System.out.println((Republica)Republicas[i]);
    }// end for
  }// end listarRepublica( )

  public static void incluirRepublica( ) throws Exception
  {
    String nome, nomeProprietario, endereco;
    float preco;

    System.out.println("\nINCLUSÃO");
    System.out.print("Nome: ");
    nome = console.nextLine();
    System.out.print("Nome Proprietário: ");
    nomeProprietario = console.nextLine();
    System.out.print("Endereço: ");
    endereco = console.nextLine();
    System.out.print("Preço (mensal): ");
    preco = Float.valueOf(console.nextLine());

    System.out.print("\nConfirma inclusão? ");
    char confirma = console.nextLine().charAt(0);

    if(confirma=='s' || confirma=='S')
    {
      Republica r = new Republica(-1, nome, nomeProprietario, endereco, preco);
      int cod = arqRepublica.incluir(r);
      System.out.println("Republica incluída com código: "+cod);
    }// end if
  }// end incluirRepublica( )

  public static void alterarRepublica( ) throws Exception
  {
    System.out.println("\nALTERAÇÃO");

    int codigo;
    System.out.print("Código: ");

    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Republica r;

    if((r = (Republica)arqRepublica.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);

      String nome, nomeProprietario, endereco, preco;

      System.out.print("\nNovo nome: ");
      nome = console.nextLine();
      System.out.print("\nNovo Proprietário: ");
      nomeProprietario = console.nextLine();
      System.out.print("Novo endereco: ");
      endereco = console.nextLine();
      System.out.print("Novo preço (mensal): ");
      preco = console.nextLine();

      System.out.print("\nConfirma alteração? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        r.nome = (nome.length()>0?nome:r.nome);
        r.nomeProprietario = (nomeProprietario.length() >0? nomeProprietario:r.nomeProprietario);
        r.endereco = (endereco.length( ) > 0? endereco: r.endereco);
        r.preco = (preco.length() > 0? Float.valueOf(preco):r.preco);

        if(arqRepublica.alterar(r))
        System.out.println("Republica alterado.");
        else
        System.out.println("Republica não pode ser alterado.");
      }// end if
    }
    else
    System.out.println("Republica não encontrado");
  }// end alterarRepublica( )

  public static void excluirRepublica( ) throws Exception
  {
    System.out.println("\nEXCLUSÃO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Republica r;
    if((r = (Republica)arqRepublica.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);
      System.out.print("\nConfirma exclusão? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        if( arqRepublica.excluir(codigo) )
        {
          System.out.println("Republica excluído.");
        }
      }
    }
    else
    System.out.println("Republica não encontrado");
  }// end excluiRepublica( )

  public static void buscarRepublicaCodigo( ) throws Exception
  {
    System.out.println("\nBUSCA POR CÓDIGO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Republica r;
    if((r = (Republica)arqRepublica.buscarCodigo(codigo))!=null)
    System.out.println(r);
    else
    System.out.println("Republica não encontrado");
  }// end buscarRepublicaCodigo( )

  public static void buscarRepublicaNome( ) throws Exception
  {
    System.out.println("\nBUSCA POR NOME");

    String nome;
    System.out.print("Nome: ");

    nome = console.nextLine();

    if(nome == "")
    return;

    Republica r;
    if((r = (Republica)arqRepublica.buscarString(nome))!=null)
    System.out.println(r);
    else
    System.out.println("Republica não encontrado");
  }// end buscarRepublicaNome( )

  public static void reorganizarRepublica( ) throws Exception
  {
    arqRepublica.reorganizar();
    System.out.println("\nArquivo de Republicas reorganizado");
  }// end reorganizar( )

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // Usuarios
  public static void listarUsuarios( ) throws Exception
  {
    Object[] Usuario = arqUsuarios.listar( );

    for(int i = 0; i < Usuario.length; i++)
    {
      System.out.println((Usuario)Usuario[i]);
    }// end for
  }// end listarRepublica( )

  public static void incluirUsuario( ) throws Exception
  {
    String nome, email, senha; // Para esconder a senha no input seria necessário trocar o input por um Console e usar console.readPassword() num char senha[].
    boolean owner = false;

    System.out.println("\nINCLUSÃO");
    System.out.print("Nome: ");
    nome = console.nextLine();
    System.out.print("Email: ");
    email = console.nextLine();
    System.out.print("Senha: ");
    senha = console.nextLine();
    //senha = Usuario.encriptar (senha);
    //senha = senha.trim();
    //System.out.println("Senha = " + senha);
    //System.out.println("Tamanho Senha = " +senha.length());

    System.out.print("\nConfirma inclusão? ");
    char confirma = console.nextLine().charAt(0);

    if(confirma=='s' || confirma=='S')
    {
      Usuario r = new Usuario(-1, nome, email, senha, owner);
      int cod = arqUsuarios.incluir(r);
      System.out.println("Usuario incluído com código: "+cod);
    }// end if
  }// end incluirRepublica( )

  public static void alterarUsuario( ) throws Exception
  {
    System.out.println("\nALTERAÇÃO");

    int codigo;
    System.out.print("Código: ");

    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Usuario r;

    if((r = (Usuario)arqUsuarios.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);

      String nome, email, senha;

      System.out.print("\nNovo nome: ");
      nome = console.nextLine();
      System.out.print("\nNovo email: ");
      email = console.nextLine();
      System.out.print("Nova senha: ");
      senha = console.nextLine();
      senha = Usuario.encriptar (senha);

      System.out.print("\nConfirma alteração? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        r.nome = (nome.length()>0?nome:r.nome);
        r.email = (email.length() >0? email:r.email);
        r.senha = (senha.length( ) > 0? senha: r.senha);
        if(arqUsuarios.alterar(r))
        System.out.println("Usuario alterado.");
        else
        System.out.println("Usuario não pode ser alterado.");
      }// end if
    }
    else
    System.out.println("Usuario não encontrado");
  }// end alterarRepublica( )

  public static void excluirUsuario( ) throws Exception
  {
    System.out.println("\nEXCLUSÃO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Usuario r;
    if((r = (Usuario)arqUsuarios.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);
      System.out.print("\nConfirma exclusão? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        if( arqUsuarios.excluir(codigo) )
        {
          System.out.println("Usuario excluído.");
        }
      }
    }
    else
    System.out.println("Usuario não encontrado");
  }// end excluiRepublica( )

  public static void buscarUsuarioCodigo( ) throws Exception
  {
    System.out.println("\nBUSCA POR CÓDIGO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Usuario r;
    if((r = (Usuario)arqUsuarios.buscarCodigo(codigo))!=null)
    System.out.println(r);
    else
    System.out.println("Usuario não encontrado");
  }// end buscarRepublicaCodigo( )

  public static void buscarUsuarioNome( ) throws Exception
  {
    System.out.println("\nBUSCA POR NOME");

    String nome;
    System.out.print("Nome: ");

    nome = console.nextLine();

    if(nome == "")
    return;

    Usuario r;
    if((r = (Usuario)arqUsuarios.buscarString(nome))!=null)
    System.out.println(r);
    else
    System.out.println("Usuario não encontrado");
  }// end buscarRepublicaNome( )

  public static void reorganizarUsuario( ) throws Exception
  {
    arqUsuarios.reorganizar();
    System.out.println("\nArquivo de Usuario reorganizado");
  }// end reorganizar( )

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // Tarefas

  public static void listarTarefas( ) throws Exception
  {
    Object[] Tarefas = arqTarefas.listar( );

    for(int i = 0; i < Tarefas.length; i++)
    {
      System.out.println((Tarefas)Tarefas[i]);
    }// end for
  }// end listarRepublica( )

  public static void incluirTarefa( ) throws Exception
  {
    String nomeTarefa, descricao, responsavel, tempoLimite, realizado;

    System.out.println("\nINCLUSÃO");
    System.out.print("Nome da Tarefa: ");
    nomeTarefa = console.nextLine();
    System.out.print("Descricao: ");
    descricao = console.nextLine();
    System.out.print("Responsavel: ");
    responsavel = console.nextLine();
    System.out.println("Tempo Limite : ");
    tempoLimite = console.nextLine();

    System.out.print("\nConfirma inclusão? ");
    char confirma = console.nextLine().charAt(0);

    if(confirma=='s' || confirma=='S')
    {
      Tarefas r = new Tarefas(-1, nomeTarefa, descricao, responsavel, tempoLimite, "false");
      int cod = arqTarefas.incluir(r);
      System.out.println("Tarefa incluído com código: "+cod);
    }// end if
  }// end incluirRepublica( )

  public static void alterarTarefa( ) throws Exception
  {
    System.out.println("\nALTERAÇÃO");

    int codigo;
    System.out.print("Código: ");

    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Tarefas r;

    if((r = (Tarefas)arqTarefas.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);

      String nomeTarefa, descricao, responsavel, tempoLimite, realizado;

      System.out.println("\nINCLUSÃO");
      System.out.print("Novo Nome da Tarefa: ");
      nomeTarefa = console.nextLine();
      System.out.print("Novo Descricao: ");
      descricao = console.nextLine();
      System.out.print("Novo Responsavel: ");
      responsavel = console.nextLine();
      System.out.println("Novo Tempo Limite : ");
      tempoLimite = console.nextLine();

      System.out.print("\nConfirma alteração? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        r.nomeTarefa = (nomeTarefa.length()>0?nomeTarefa:r.nomeTarefa);
        r.descricao = (descricao.length() >0? descricao:r.descricao);
        r.responsavel = (responsavel.length( ) > 0? responsavel: r.responsavel);
        r.tempoLimite = (tempoLimite.length( ) > 0? tempoLimite: r.tempoLimite);
        if(arqTarefas.alterar(r))
        System.out.println("Tarefa alterado.");
        else
        System.out.println("Tarefa não pode ser alterado.");
      }// end if
    }
    else
    System.out.println("Tarefa não encontrado");
  }// end alterarRepublica( )

  public static void excluirTarefa( ) throws Exception
  {
    System.out.println("\nEXCLUSÃO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Tarefas r;
    if((r = (Tarefas)arqTarefas.buscarCodigo(codigo))!=null)
    {
      System.out.println(r);
      System.out.print("\nConfirma exclusão? ");
      char confirma = console.nextLine().charAt(0);

      if(confirma=='s' || confirma=='S')
      {
        if( arqTarefas.excluir(codigo) )
        {
          System.out.println("Tarefa excluído.");
        }
      }
    }
    else
    System.out.println("Tarefa não encontrado");
  }// end excluiRepublica( )

  public static void buscarTarefaCodigo( ) throws Exception
  {
    System.out.println("\nBUSCA POR CÓDIGO");

    int codigo;
    System.out.print("Código: ");
    codigo = Integer.valueOf(console.nextLine());

    if(codigo <=0)
    return;

    Tarefas r;
    if((r = (Tarefas)arqTarefas.buscarCodigo(codigo))!=null)
    System.out.println(r);
    else
    System.out.println("Tarefas não encontrado");
  }// end buscarRepublicaCodigo( )

  public static void buscarTarefaDescricao( ) throws Exception
  {
    System.out.println("\nBUSCA POR DESCRICAO");

    String descricao;
    System.out.print("Descricao: ");

    descricao = console.nextLine();

    if(descricao == "")
    return;

    Tarefas r;
    if((r = (Tarefas)arqTarefas.buscarString(descricao))!=null)
    System.out.println(r);
    else
    System.out.println("Tarefas não encontrado");
  }// end buscarRepublicaNome( )

  public static void reorganizarTarefas( ) throws Exception
  {
    arqTarefas.reorganizar();
    System.out.println("\nArquivo de Tarefas reorganizado");
  }// end reorganizar( )

  public static void reorganizar() throws Exception
  {
    System.out.println("Reorganizando Arquivos");
    reorganizarRepublica();
    reorganizarUsuario();
    reorganizarTarefas();
    reorganizarGastos();
    System.out.println("Reorganizacao completa.");
  }

  public static void listarUsuariosRepublica() throws Exception {

    System.out.println("\nLISTA DE USUARIOS POR REPUBLICA");

    int codigo;
    System.out.print("Código da republica: ");
    codigo = Integer.valueOf(console.nextLine());
    if(codigo <=0)
    return;

    int[] lista = idxUsuarioRepublica.lista(codigo);
    Usuario obj;
    for(int i=0; i<lista.length; i++) {
      if( (obj = (Usuario)arqUsuarios.buscarCodigo(lista[i]))!=null )
      System.out.println(
      "\nCodigo...........:" + obj.idUsuario           +
      "\nNome............:" + obj.nome             +
      "\nEmail...........:" + obj.email
      );
      else
      System.out.println("Usuario não encontrado");
    }
  }

  public static void listarRepublicasUsuario() throws Exception {

    System.out.println("\nLISTA DE REPUBLICAS POR USUARIO");

    int codigo;
    System.out.print("Código do usuario: ");
    codigo = Integer.valueOf(console.nextLine());
    if(codigo <=0)
    return;

    int[] lista = idxRepublicaUsuario.lista(codigo);
    Republica obj;
    for(int i=0; i<lista.length; i++) {
      if( (obj = (Republica)arqRepublica.buscarCodigo(lista[i]))!=null )
      System.out.println(
      "\nCodigo...........:" + obj.codigo           +
      "\nNome............:" + obj.nome             +
      "\nProprietario.......:" + obj.nomeProprietario +
      "\nEndereco...........:" + obj.endereco +
      "\nPreco.......:" + obj.preco
      );
      else
      System.out.println("Republica não encontrada");
    }
  }

  public static void listarUsuariosTarefa() throws Exception {

    System.out.println("\nLISTA DE USUARIOS POR TAREFA");

    int codigo;
    System.out.print("Código da tarefa: ");
    codigo = Integer.valueOf(console.nextLine());
    if(codigo <=0)
    return;

    int[] lista = idxUsuarioTarefas.lista(codigo);
    Usuario obj;
    for(int i=0; i<lista.length; i++) {
      if( (obj = (Usuario)arqUsuarios.buscarCodigo(lista[i]))!=null )
      System.out.println(
      "\nCodigo...........:" + obj.idUsuario           +
      "\nNome............:" + obj.nome             +
      "\nEmail...........:" + obj.email
      );
      else
      System.out.println("Usuarios não encontrados");
    }
  }

  // Metodos a fazer

  public static void listarTarefasUsuario()
  {

  }

  public static void listarTarefasRepublica()
  {

  }

  public static void reorganizarGastos() throws Exception
  {

  }

  public static void listarGastosRepublica()
  {

  }
  // Povoar
  public static void povoar( ) throws Exception
  {
    arqRepublica.incluir(new Republica(-1, "Pecado Original", "João Paulo", "Rua 31 de Março", (float)50));
    arqRepublica.incluir(new Republica(-1, "Chocolate com Pimenta", "Pedro Henrique", "Rua Getúlio Vargas", (float)60));
    arqRepublica.incluir(new Republica(-1, "República Off Road", "Luiz Gustavo", "R. Nina Sanzi - Calafate", (float)70));
    arqRepublica.incluir(new Republica(-1, "República Carranca", "Rafael Zuckenberg", "R. Des. Paulo Mota, 228 - Ouro Preto", (float)35));
    arqRepublica.incluir(new Republica(-1, "República de Minas Gerais", "Zé Garcia", "Rua 25 de Março", (float)44));
    arqRepublica.incluir(new Republica(-1, "Poore Simon", "Martin L. Hinojos", "Avenida Érico Veríssimo, 1199 Gurupi-TO 77445-080", (float)27.9));
    arqRepublica.incluir(new Republica(-1, "Republica Limpúria", "Carlos T. McClean", "Avenida Independência, 674 Goiânia-GO 74645-010", (float)54.0));
    arqRepublica.incluir(new Republica(-1, "Crandall's Republic for Immigrants", "Robert A. Taylor", "Rua Tomás Mobilli, 1061 Votorantim-SP 18110-460", (float)10.5));
    arqRepublica.incluir(new Republica(-1, "Republica do Rio", "Paula R. Silva", "Rua Santo Amadeu de Sabóia, 1235 Rio de Janeiro-RJ 21361-350", (float)12.65));
  }// end povoar( )
}// end class ControleDeRepublicas
