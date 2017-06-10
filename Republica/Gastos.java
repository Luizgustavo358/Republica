import java.io.*;
// ler https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
public class Gastos implements Registro
{
      // definir dados
      protected int codigoGasto;
      protected String nomeGasto;
      protected float valor;
      protected String data;
      protected String descricao;

      protected boolean debug = true;

      public Gastos(String n, float v, String d, String desc, int codigo)
      {
            nomeGasto = n;
            valor     = v;
            data      = d;
            descricao = desc;
            codigoGasto = codigo;
      }// end construtor

      public Gastos( )
      {
            nomeGasto = "";
            valor = 0;
            data = "";
            descricao = "";
            codigoGasto = 0;
      }// end construtor

      public void setCodigo(int codigo)
      {
            codigoGasto = codigo;
      }// end setCodigo( )

      public int getCodigo( )
      {
            return codigoGasto;
      }// end getCodigo( )

      public float getValor( )
      {
            return valor;
      }// end getValor( )

      public String getData( )
      {
            return data;
      }

      public String getDescricao( )
      {
            return descricao;
      }

      public String getString( )
      {
            return nomeGasto;
      }// end getString( )

      public String toString( )
      {
            return "\nNome Gasto.......:" + nomeGasto +
            "\nValor............:" + valor     +
            "\nData.............:" + data      +
            "\nDescricao........:" + descricao;
      }// end toString( )

      public byte[] getByteArray( ) throws IOException
      {
            ByteArrayOutputStream registro = new ByteArrayOutputStream( );
            DataOutputStream saida = new DataOutputStream(registro);
            saida.writeUTF(nomeGasto);
            saida.writeFloat(valor);
            saida.writeUTF(data);
            saida.writeUTF(descricao);
            return registro.toByteArray();
      }// end getByteArray( )

      public void setByteArray(byte[] b) throws IOException
      {
            ByteArrayInputStream registro = new ByteArrayInputStream(b);
            DataInputStream entrada = new DataInputStream(registro);
            nomeGasto = entrada.readUTF();
            valor = entrada.readFloat();
            data = entrada.readUTF();
            descricao = entrada.readUTF();
      }// end setByteArray( )

      public Object clone( ) throws CloneNotSupportedException
      {
            return super.clone();
      }// end clone( )

      public int compareTo( Object b )
      {
            return codigoGasto - ((Gastos)b).codigoGasto;
      }// end compareTo( )

      /*
      public int compareTo( Object b ) {
      return nome.compareTo(((Usuario)b).nome);
}
*/
}// end class Gastos
