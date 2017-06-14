import java.io.*;
// ler https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
public class Tarefas implements Registro
{
   // definir dados
   protected int idTarefa;
   protected String nomeTarefa;
   protected String descricao;
   protected String tempoLimite;
   protected String realizado;
   protected int idRepublica;
   protected int[] idUsuario;
   protected int numUsuario;

   protected boolean debug = false;

   public Tarefas(int t, String n, String d, String tl, String rz, int id1, int num, int[] id2)
   {
      idTarefa    = t;
      nomeTarefa  = n;
      descricao   = d;
      tempoLimite = tl;
      realizado   = rz;
      idRepublica = id1;
      numUsuario = num;
      idUsuario = id2;
   }// end construtor

   public Tarefas( )
   {
      idTarefa    = 0;
      nomeTarefa  = "";
      descricao   = "";
      tempoLimite = "";
      realizado   = "";
      idRepublica = 0;
      numUsuario = 1;
      idUsuario = new int[] {-1};
   }// end construtor

   public void setCodigo(int t)
   {
      idTarefa = t;
   }// end setCodigo( )

   public int getCodigo( )
   {
      return idTarefa;
   }// end getCodigo( )

   public String getString( )
   {
      return nomeTarefa;
   }// end getString( )

   public String getDescricao( )
   {
      return descricao;
   }// end getDescricao( )

   public String getTempoLimite( ) // republica na qual o usuario pertence
   {
      return tempoLimite;
   }// end getTempoLimite

   public String getRealizado( )
   {
      return realizado;
   }// end getRealizado( )

   public String toString( )
   {
      String resp = "\nCodigo...........:" + idTarefa    +
                 "\nTarefa...........:" + nomeTarefa  +
                 "\nDescricao........:" + descricao   +
                 "\nTempo Limite.....:" + tempoLimite +
                 "\nRealizado........:" + realizado   +
                 "\nCodigo Republica.:" + idRepublica;
   
      for (int x = 0; x < idUsuario.length; x++)
      {
         resp += "\nCodigo Usuarios: " + idUsuario[x];
      }
      return resp;
   }// end toString( )

   public byte[] getByteArray( ) throws IOException
   {
      ByteArrayOutputStream registro = new ByteArrayOutputStream( );
      DataOutputStream saida = new DataOutputStream(registro);
      saida.writeInt(idTarefa);
      saida.writeUTF(nomeTarefa);
      saida.writeUTF(descricao);
      saida.writeUTF(tempoLimite);
      saida.writeUTF(realizado);
      saida.writeInt(idRepublica);
      saida.writeInt(numUsuario);
      for (int x = 0; x < idUsuario.length; x++)
         saida.writeInt(idUsuario[x]);
      return registro.toByteArray();
   }// end getByteArray( )

   public void setByteArray(byte[] b) throws IOException
   {
      ByteArrayInputStream registro = new ByteArrayInputStream(b);
      DataInputStream entrada = new DataInputStream(registro);
      int temp [];
      idTarefa = entrada.readInt();
      nomeTarefa = entrada.readUTF();
      descricao = entrada.readUTF();
      tempoLimite = entrada.readUTF();
      realizado = entrada.readUTF();
      this.idRepublica = entrada.readInt();
      this.numUsuario = entrada.readInt();
      temp = new int [numUsuario];
      for (int x = 0; x < numUsuario; x++)
         temp[x] = entrada.readInt();
      this.idUsuario = temp;
   }// end setByteArray( )

   public Object clone( ) throws CloneNotSupportedException
   {
      return super.clone();
   }// end clone( )

   public int compareTo( Object b )
   {
      return idTarefa - ((Tarefas)b).idTarefa;
   }// end compareTo( )

   /*
   public int compareTo( Object b ) {
         return nome.compareTo(((Usuario)b).nome);
   }*/
}// end class Tarefas
