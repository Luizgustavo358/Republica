import java.io.*;

public class Republica implements Registro
{
      // definir dados
      protected int codigo;
      protected String nome;
      protected String nomeProprietario;
      protected String endereco;
      protected float preco;

      public Republica(int c, String n, String np, String e, float p)
      {
            codigo = c;
            nome = n;
            nomeProprietario = np;
            endereco = e;
            preco = p;
      }// end construtor

      public Republica( )
      {
            codigo = 0;
            nome = "";
            nomeProprietario = "";
            endereco = "";
            preco = 0;
      }// end construtor

      public void setCodigo(int c)
      {
            codigo = c;
      }// end setCodigo( )

      public int getCodigo( )
      {
            return codigo;
      }// end getCodigo( )

      public String getString( )
      {
            return nome;
      }// end getString( )

      public String toString( )
      {
            return "\nCódigo..........:" + codigo           +
            "\nNome...........:" + nome             +
            "\nProprietário...:" + nomeProprietario +
            "\nEndereco.......:" + endereco         +
            "\nPreço..........:" + preco;
      }// end toString( )

      public byte[] getByteArray( ) throws IOException
      {
            ByteArrayOutputStream registro = new ByteArrayOutputStream( );
            DataOutputStream saida = new DataOutputStream(registro);
            saida.writeInt(codigo);
            saida.writeUTF(nome);
            saida.writeUTF(nomeProprietario);
            saida.writeUTF(endereco);
            saida.writeFloat(preco);
            return registro.toByteArray();
      }// end getByteArray( )

      public void setByteArray(byte[] b) throws IOException
      {
            ByteArrayInputStream registro = new ByteArrayInputStream(b);
            DataInputStream entrada = new DataInputStream(registro);
            codigo = entrada.readInt();
            nome = entrada.readUTF();
            nomeProprietario = entrada.readUTF( );
            endereco = entrada.readUTF();
            preco = entrada.readFloat();
      }// end setByteArray( )

      public Object clone( ) throws CloneNotSupportedException
      {
            return super.clone();
      }// end clone( )

      public int compareTo( Object b )
      {
            return codigo - ((Republica)b).codigo;
      }// end compareTo( )

      /*
      public int compareTo( Object b ) {
      return nome.compareTo(((Republica)b).nome);
}
*/
}// end class Republica
