import java.io.*;
// ler https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
public class Tarefas implements Registro
{
      // definir dados
      protected int idTarefa;
      protected String nomeTarefa;
      protected String descricao;
      protected String responsavel;
      protected String tempoLimite;
      protected String realizado;

      protected boolean debug = true;

      public Tarefas(int t, String n, String d, String r, String tl, String rz)
      {
            idTarefa    = t;
            nomeTarefa  = n;
            descricao   = d;
            responsavel = r;
            tempoLimite = tl;
            realizado   = rz;
      }// end construtor

      public Tarefas( )
      {
            idTarefa    = 0;
            nomeTarefa  = "";
            descricao   = "";
            responsavel = "";
            tempoLimite = "";
            realizado   = "";
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

      public String getResponsavel( )
      {
            return responsavel;
      }// end getResponsavel( )

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
            return "\nCodigo...........:" + idTarefa    +
                   "\nTarefa...........:" + nomeTarefa  +
                   "\nDescricao........:" + descricao   +
                   "\nResponsavel......:" + responsavel +
                   "\nTempo Limite.....:" + tempoLimite +
                   "\nRealizado........:" + realizado;
      }// end toString( )

      public byte[] getByteArray( ) throws IOException
      {
            ByteArrayOutputStream registro = new ByteArrayOutputStream( );
            DataOutputStream saida = new DataOutputStream(registro);
            saida.writeInt(idTarefa);
            saida.writeUTF(nomeTarefa);
            saida.writeUTF(descricao);
            saida.writeUTF(responsavel);
            saida.writeUTF(tempoLimite);
            saida.writeUTF(realizado);
            return registro.toByteArray();
      }// end getByteArray( )

      public void setByteArray(byte[] b) throws IOException
      {
            ByteArrayInputStream registro = new ByteArrayInputStream(b);
            DataInputStream entrada = new DataInputStream(registro);
            idTarefa = entrada.readInt();
            nomeTarefa = entrada.readUTF();
            descricao = entrada.readUTF();
            responsavel = entrada.readUTF();
            tempoLimite = entrada.readUTF();
            realizado = entrada.readUTF();
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