import java.io.*;
// ler https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
public class Usuario implements Registro
{
   // definir dados
   protected int idUsuario;
   protected String nome;
   protected String email; // Tambem e o login.
   protected String senha; // guardada ja encriptada.
   protected boolean isOwner; // False = Nao e proprietario.

   protected static String chave = "abacate";
   protected boolean debug = true;

   public Usuario(int c, String n, String em, String s, boolean ow)
   {
      idUsuario = c;
      nome = n;
      email = em;
      this.senha = senha;
      isOwner = ow;
   }// end construtor

   public Usuario( )
   {
      idUsuario = -1;
      nome = "";
      senha = "";
      email = "";
      isOwner = false;
   }// end construtor

   public void setCodigo(int c)
   {
      idUsuario = c;
   }// end setCodigo( )

   public int getCodigo( )
   {
      return idUsuario;
   }// end getCodigo( )

   public String getString( )
   {
      return nome;
   }// end getString( )

   public String getEmail ( )
   {
      return email;
   }

   public String getSenha ( )
   {
      if (debug == true)
         return descriptografar (senha);
      else
         return "Acesso negado";
   }

   public String toString( )
   {
      return 
         "\nCodigo.........:" + idUsuario +
         "\nNome...........:" + nome      +
         "\nEmail..........:" + email     +
         "\nSenha..........:" + getSenha();
   }// end toString( )

   public static String encriptar (String texto)
   {
      String encriptado;
      int pos = 0;
      int tamanhoTexto = texto.length();
      int tamanhoChave = chave.length();
      int linhas = (int)Math.ceil ((float)tamanhoTexto / (float)tamanhoChave);
   
      char tabela[][] = new char[linhas][tamanhoChave]; // [linhas][colunas]
   
      for (int x = 0; x < tabela.length; x++)
      {
         for (int y = 0; y < tabela[x].length && pos < tamanhoTexto; y++)
         {
            tabela[x][y] = (char) (( texto.charAt(pos) + chave.charAt(pos % tamanhoChave) ) ); // vai criptografar de acordo com o charset definido (UTF-8).
            pos++;
         } // end for (y)
      } // end for (x)
      encriptado = TabTexto (tabela);
      return encriptado;
   } // end encriptar

   protected String descriptografar (String senha)
   {
      String descriptografado = "";
   
      int tamanhoChave = chave.length();
      int tamanhoTexto = senha.length();
      int linhas = (int)Math.ceil ((float)tamanhoTexto / (float)tamanhoChave);
      int pos = 0;
   
      char tabela[][] = new char[linhas][tamanhoChave]; // [linhas][colunas]
   
      for ( int x = 0; x < tabela.length; x++)
      {
         for (int y = 0; y < tabela[x].length; y++)
         {
            if (senha.charAt(pos) != '\u0000') // '\u0000' = null
               tabela[x][y] = (char) (( senha.charAt(pos) - chave.charAt(pos % tamanhoChave) ) ); // vai descriptografar de acordo com o charset definido (UTF-8).
            pos++;
         } // end for (y)
      } // end for (x)
      descriptografado = TabTexto(tabela);
      return descriptografado;
   } // end descriptografar

   public static String TabTexto (char tabela[][]) // metodo para transformar uma tabela em String
   {
      String texto = "";
      for (int x = 0; x < tabela.length; x++)
      {
         for (int y = 0; y < tabela[x].length; y++)
         {
            texto += Character.toString(tabela[x][y]);
         } // end for y
      }
      return texto;
   } // end TabTexto

   public byte[] getByteArray( ) throws IOException
   {
      ByteArrayOutputStream registro = new ByteArrayOutputStream( );
      DataOutputStream saida = new DataOutputStream(registro);
      saida.writeInt(idUsuario);
      saida.writeUTF(nome);
      saida.writeUTF(email);
      saida.writeUTF(senha);
      saida.writeBoolean(isOwner);
      return registro.toByteArray();
   }// end getByteArray( )

   public void setByteArray(byte[] b) throws IOException
   {
      ByteArrayInputStream registro = new ByteArrayInputStream(b);
      DataInputStream entrada = new DataInputStream(registro);
      idUsuario = entrada.readInt();
      nome = entrada.readUTF();
      email = entrada.readUTF();
      senha = entrada.readUTF();
      isOwner = entrada.readBoolean();
   }// end setByteArray( )

   public Object clone( ) throws CloneNotSupportedException
   {
      return super.clone();
   }// end clone( )

   public int compareTo( Object b )
   {
      return idUsuario - ((Usuario)b).idUsuario;
   }// end compareTo( )

   /*
   public int compareTo( Object b ) {
     return nome.compareTo(((Usuario)b).nome);
   }*/
}// end class Usuario
