import java.io.*;

public class ArvoreB_Indireto
{
   // definir dados
   private final int   ordem;
   private RandomAccessFile arquivo;
   private final String nomeArquivo;

   // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
   private String  chaveExtra;
   private int     dadosExtra;
   private long    paginaExtra;
   private boolean cresceu;
   private boolean diminuiu;

   class ArvoreB_Pagina
   {
      // definir dados
      protected int       ordem;
      protected int       n;
      protected String[]  chaves;   // um campo string -> ESTE ÍNDICE É DE UM NOME, TÍTULO OU ALGO ASSIM
      protected int[]     dados;    // o código do registro -> ESTE ÍNDICE É INDIRETO
      protected long[]    filhos;
      private final int   TAMANHO_CHAVE = 100; // tamanho da string que será usada como chave
      private final int   TAMANHO_REGISTRO = TAMANHO_CHAVE+4;
      protected final int TAMANHO_PAGINA;

      public ArvoreB_Pagina(int o)
      {
         n = 0;
         ordem = o;
         chaves = new String[ordem*2];
         dados = new int[ordem*2];
         filhos = new long[ordem*2+1];

         for(int i = 0; i < ordem*2; i++)
         {
            chaves[i] = "";
            dados[i] = -1;
            filhos[i] = -1;
         }// end for

         filhos[ordem*2] = -1;
         TAMANHO_PAGINA = 4 + (ordem*2)*TAMANHO_REGISTRO + (ordem*2+1)*8;
      }// end construtor

      private byte[] completaBrancos(String str)
      {
         byte[] aux;
         byte[] buffer = new byte[TAMANHO_CHAVE];
         aux = str.getBytes( );
         int i=0;

         while(i < aux.length)
         {
            buffer[i] = aux[i];
            i++;
         }// end while

         while(i < TAMANHO_CHAVE)
         {
            buffer[i] = 0x20;
            i++;
         }// end while

         return buffer;
      }// end completaBrancos( )

      protected byte[] getBytes( ) throws IOException
      {
         ByteArrayOutputStream ba = new ByteArrayOutputStream( );
         DataOutputStream out = new DataOutputStream(ba);
         out.writeInt(n);

         int i=0;

         while(i < n)
         {
            out.writeLong(filhos[i]);
            out.write(completaBrancos(chaves[i]));
            out.writeInt(dados[i]);
            i++;
         }// end while

         out.writeLong(filhos[i]);
         byte[] registroVazio = new byte[TAMANHO_REGISTRO];

         while(i < ordem*2)
         {
            out.write(registroVazio);
            out.writeLong(filhos[i+1]);
            i++;
         }// end while

         return ba.toByteArray( );
      }// end getBytes( )

      public void setBytes(byte[] buffer) throws IOException
      {
         ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
         DataInputStream in = new DataInputStream(ba);
         byte[] bstr = new byte[TAMANHO_CHAVE];
         n = in.readInt();

         int i = 0;

         while(i < n)
         {
            filhos[i] = in.readLong( );
            in.read(bstr);
            chaves[i] = (new String(bstr)).trim( );
            dados[i] = in.readInt( );
            i++;
         }// end while

         filhos[i] = in.readLong( );
      }// end setBytes( )
   }// end class

   public ArvoreB_Indireto(int o, String na) throws IOException
   {
      ordem = o;
      nomeArquivo = na;
      arquivo = new RandomAccessFile(nomeArquivo,"rw");

      if(arquivo.length() < 8)
         arquivo.writeLong(-1);  // raiz vazia
   }// end construtor

   // Testa se a árvore está vazia
   public boolean vazia( ) throws IOException
   {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong( );

      return raiz == -1;
   }// end vazia( )

   // Método para buscar
   public int buscar(String c) throws IOException
   {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong( );

      if(raiz != -1)
         return buscar1(c,raiz);
      else
         return -1;
   }// end buscar( )

   private int buscar1(String chaveBusca, long pagina) throws IOException
   {
      if(pagina == -1)
         return -1;

      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);

      // Encontra (recursivamente) a página para inserção
      int i = 0;

      while(i < pa.n && chaveBusca.compareTo(pa.chaves[i]) > 0)
      {
         i++;
      }// end while

      if(i < pa.n)
      {
         if(chaveBusca.compareTo(pa.chaves[i].substring(0,Math.min(chaveBusca.length( ),pa.chaves[i].length()))) == 0)
         {
            // registro encontrado
            return pa.dados[i];
         }// end if

         if(chaveBusca.compareTo(pa.chaves[i]) < 0)
            return buscar1(chaveBusca, pa.filhos[i]);
         else
            return buscar1(chaveBusca, pa.filhos[i+1]);
      }
      else
      {
         return buscar1(chaveBusca, pa.filhos[i]);
      }// end if
   }// end busca1( )

   // Método para atualizar o campo de dados
   public boolean atualizar(String c, int e) throws IOException
   {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong( );

      if(raiz != -1)
         return atualizar1(c,e,raiz);
      else
         return false;
   }// end atualizar( )

   private boolean atualizar1(String chaveBusca, int dadosAtualizados, long pagina) throws IOException
   {
      if(pagina == -1)
         return false;

      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);

      // Encontra (recursivamente) a página para inserção
      int i = 0;

      while(i < pa.n && chaveBusca.compareTo(pa.chaves[i]) > 0)
      {
         i++;
      }// end while

      if(i < pa.n)
      {
         if(chaveBusca.compareTo(pa.chaves[i]) == 0)
         {
            // registro encontrado
            pa.dados[i] = dadosAtualizados;
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            return true;
         }// end if

         if(chaveBusca.compareTo(pa.chaves[i]) < 0)
            return atualizar1(chaveBusca, dadosAtualizados, pa.filhos[i]);
         else
            return atualizar1(chaveBusca, dadosAtualizados, pa.filhos[i+1]);
      }
      else
      {
         return atualizar1(chaveBusca, dadosAtualizados, pa.filhos[i]);
      }// end if
   }// end atualizar1( )

   // Método para inclusão -> transforma a chamada em um função recursiva a partir da raoz
   public void inserir(String c, int e) throws IOException
   {
      arquivo.seek(0);
      long pagina;        // carrega a raiz como primeira página
      pagina = arquivo.readLong();

      chaveExtra  = c;    // converte chave e dados para tipos de referência, para que possam ser atualizados pela função recursiva
      dadosExtra  = e;
      paginaExtra = -1;   // ponteiro para a página filho direito do registro promovido
      cresceu = false;    // controla se houve crescimento da árvore

      // Inclui o registro (na chaveExtra e com os dados) na página
      inserir1(pagina);

      // Testa a necessidade de criação de uma nova raiz
      if(cresceu)
      {
         ArvoreB_Pagina novaPagina = new ArvoreB_Pagina(ordem);
         novaPagina.n = 1;
         novaPagina.chaves[0] = chaveExtra;
         novaPagina.dados[0]  = dadosExtra;
         novaPagina.filhos[0] = pagina;
         novaPagina.filhos[1] = paginaExtra;

         // Achar o espaço em disco....
         arquivo.seek(arquivo.length( ));
         long raiz = arquivo.getFilePointer( );
         arquivo.write(novaPagina.getBytes( ));
         arquivo.seek(0);
         arquivo.writeLong(raiz);
      }// end if
   }// end inserir( )

   private void inserir1(long pagina) throws IOException
   {
      // testa se atingiu uma página folha. Caso afirmativo, cria o registro e o devolve para ser incluído
      if(pagina == -1)
      {
         cresceu = true;
         paginaExtra = -1;
         return;
      }// end if

      // Lê a página
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);

      // Encontra (recursivamente) a página para inserção
      int i = 0;

      while(i < pa.n && chaveExtra.compareTo(pa.chaves[i]) > 0)
      {
         i++;
      }// end while

      if(i < pa.n)
      {
         if(chaveExtra.compareTo(pa.chaves[i]) == 0)
         {
            // registro já existe
            cresceu = false;
            return;
         }// end if

         if(chaveExtra.compareTo(pa.chaves[i]) < 0)
            inserir1(pa.filhos[i]);
         else
            inserir1(pa.filhos[i+1]);
      }
      else
      {
         inserir1(pa.filhos[i]);
      }// end if

      // Controle o retorno das chamadas recursivas sem a inclusão de nova página (se o registro já existir ou couber em página existente)
      if(!cresceu)
         return;

      // Se tiver espaço na página
      if(pa.n < ordem*2)
      {
         for(int j = pa.n; j > i; j--)
         {
            pa.chaves[j]   = pa.chaves[j-1];
            pa.dados[j]    = pa.dados[j-1];
            pa.filhos[j+1] = pa.filhos[j];
         }// end for

         pa.chaves[i]   = chaveExtra;
         pa.dados[i]    = dadosExtra;
         pa.filhos[i+1] = paginaExtra;
         pa.n++;
         arquivo.seek(pagina);
         arquivo.write(pa.getBytes( ));
         cresceu=false;

         return;
      }// end if

      // Overflow: divide a página e move metade dos registros
      ArvoreB_Pagina np = new ArvoreB_Pagina(ordem);

      for(int j = 0; j < ordem; j++)
      {
         np.chaves[j]   = pa.chaves[j+ordem];
         np.dados[j]    = pa.dados[j+ordem];
         np.filhos[j+1] = pa.filhos[j+ordem+1];
      }// end for

      np.filhos[0] = pa.filhos[ordem];
      np.n = ordem;
      pa.n = ordem;

      // Testa o lado de inserção
      if(i <= ordem)
      {
         // novo registro deve ficar na página da esquerda
         if(i == ordem)
         {
            // Testa se é o próprio registro que será promovido
            np.filhos[0] = paginaExtra;
         }
         else
         {
            String c_aux = pa.chaves[ordem-1];
            int    e_aux = pa.dados[ordem-1];

            for(int j = ordem; j > 0 && j > i; j--)
            {
               pa.chaves[j]   = pa.chaves[j-1];
               pa.dados[j]    = pa.dados[j-1];
               pa.filhos[j+1] = pa.filhos[j];
            }// end for

            pa.chaves[i]   = chaveExtra;
            pa.dados[i]    = dadosExtra;
            pa.filhos[i+1] = paginaExtra;
            chaveExtra     = c_aux;
            dadosExtra     = e_aux;
         }// end if
      }
      else
      {
         String c_aux = np.chaves[0];
         int    e_aux = np.dados[0];
         int j;

         for(j = 0; j < ordem-1 && np.chaves[j+1].compareTo(chaveExtra) < 0; j++)
         {
            np.chaves[j] = np.chaves[j+1];
            np.dados[j] = np.dados[j+1];
            np.filhos[j] = np.filhos[j+1];
         }// end for

         np.filhos[j]   = np.filhos[j+1];
         np.chaves[j]   = chaveExtra;
         np.dados[j]    = dadosExtra;
         np.filhos[j+1] = paginaExtra;
         chaveExtra     = c_aux;
         dadosExtra     = e_aux;
      }// end if

      arquivo.seek(pagina);
      arquivo.write(pa.getBytes( ));
      arquivo.seek(arquivo.length( ));
      paginaExtra = arquivo.getFilePointer( );
      arquivo.write(np.getBytes( ));
   }// end inserir1( )

   public boolean excluir(String chaveParaExcluir) throws IOException
   {
      arquivo.seek(0);
      long pagina;       // carrega a raiz como primeira página
      pagina = arquivo.readLong( );

      diminuiu = false;  // controla se houve redução da árvore

      // Inclui o registro (na chaveExtra e no dadosExtra) na página
      boolean excluido = excluir1(chaveParaExcluir, pagina);

      if(excluido && diminuiu)
      {
         arquivo.seek(pagina);
         ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
         byte[] buffer = new byte[pa.TAMANHO_PAGINA];
         arquivo.read(buffer);
         pa.setBytes(buffer);

         if(pa.n == 0)
         {
            arquivo.seek(0);
            arquivo.writeLong(pa.filhos[0]);  // Atualiza raiz. A raiz antiga deveria ser encaixada na lista de espaços disponíveis
         }// end if
      }// end if

      return excluido;
   }// end excluir( )

   private boolean excluir1(String chaveParaExcluir, long pagina) throws IOException
   {
      boolean excluido;
      int diminuido;

      // Testa se o registro não foi encontrado na árvore
      if(pagina == -1)
      {
         diminuiu = false;
         return false;
      }// end if

      // Lê a página no disco
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);

      // Encontra (recursivamente) a página para inserção
      int i = 0;

      while(i < pa.n && chaveParaExcluir.compareTo(pa.chaves[i]) > 0)
      {
         i++;
      }// end while

      if(i < pa.n)
      {
         if(chaveParaExcluir.compareTo(pa.chaves[i]) == 0)
         {
            // registro encontrado
            // Testa se a exclusão é em uma folha
            if(pa.filhos[i] == -1)
            {
               // "puxa" os demais registros
               int j;

               for(j = i; j < pa.n-1; j++)
               {
                  pa.chaves[j] = pa.chaves[j+1];
                  pa.dados[j]  = pa.dados[j+1];
               }// end for

               pa.n--;
               arquivo.seek(pagina);
               arquivo.write(pa.getBytes( ));
               diminuiu = pa.n<ordem;

               return true;
            }
            else
            {
               // exclusão não é em uma folha
               // encontra o maior antecessor do registro
               long paginaAux = pa.filhos[i];
               ArvoreB_Pagina paux = new ArvoreB_Pagina(ordem);

               while(paginaAux != -1)
               {
                  arquivo.seek(paginaAux);
                  arquivo.read(buffer);
                  paux.setBytes(buffer);
                  paginaAux = paux.filhos[paux.n];
               }// end while

               String chaveAntecessor = paux.chaves[paux.n-1];
               int    dadosAntecessor = paux.dados[paux.n-1];

               // muda a exclusão da chave atual para exclusão na folha
               pa.chaves[i] = chaveAntecessor;
               pa.dados[i]  = dadosAntecessor;
               arquivo.seek(pagina);
               arquivo.write(pa.getBytes());
               excluido  = excluir1(chaveAntecessor,pa.filhos[i]);
               diminuido = i;
            }// end if
         }
         else
         {
            if(chaveParaExcluir.compareTo(pa.chaves[i]) < 0)
            {
               excluido  = excluir1(chaveParaExcluir, pa.filhos[i]);
               diminuido = i;
            }
            else
            {
               excluido  = excluir1(chaveParaExcluir, pa.filhos[i+1]);
               diminuido = i+1;
            }// end if
         }// end if
      }
      else
      {
         excluido = excluir1(chaveParaExcluir, pa.filhos[i]);
         diminuido = i;
      }// end if

      // Testa se houve redução de nós
      if(diminuiu)
      {
         long paginaFilho = pa.filhos[diminuido];
         ArvoreB_Pagina pFilho = new ArvoreB_Pagina(ordem);
         arquivo.seek(paginaFilho);
         arquivo.read(buffer);
         pFilho.setBytes(buffer);

         long paginaIrmao;
         ArvoreB_Pagina pIrmao;

         // Testa fusão com irmão esquerdo
         if(diminuido > 0)
         {
            paginaIrmao = pa.filhos[diminuido-1];
            pIrmao = new ArvoreB_Pagina(ordem);
            arquivo.seek(paginaIrmao);
            arquivo.read(buffer);
            pIrmao.setBytes(buffer);

            // Testa se o irmão pode emprestar algum registro
            if(pIrmao.n > ordem)
            {
               // move todos os registros no filho para a direita
               for(int j = pFilho.n; j > 0; j--)
               {
                  pFilho.chaves[j]   = pFilho.chaves[j-1];
                  pFilho.dados[j]    = pFilho.dados[j-1];
                  pFilho.filhos[j+1] = pFilho.filhos[j];
               }// end for

               pFilho.filhos[1] = pFilho.filhos[0];
               pFilho.n++;

               // desce o elemento pai
               pFilho.chaves[0] = pa.chaves[diminuido-1];
               pFilho.dados[0]  = pa.dados[diminuido-1];

               // sobe o elemento do irmão
               pa.chaves[diminuido-1] = pIrmao.chaves[pIrmao.n-1];
               pa.dados[diminuido-1]  = pIrmao.dados[pIrmao.n-1];
               pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
               pIrmao.n--;
               diminuiu = false;
            }
            else // Se não puder emprestar, faz a fusão dos dois irmãos
            {
               // Desce o registro no pai para o irmão da esquerda
               pIrmao.chaves[pIrmao.n]   = pa.chaves[diminuido-1];
               pIrmao.dados[pIrmao.n]    = pa.dados[diminuido-1];
               pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[0];
               pIrmao.n++;

               // copia todos os registros para o irmão da esquerda
               for(int j = 0; j < pFilho.n; j++)
               {
                  pIrmao.chaves[pIrmao.n]   = pFilho.chaves[j];
                  pIrmao.dados[pIrmao.n]    = pFilho.dados[j];
                  pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[j+1];
                  pIrmao.n++;
               }// end for

               pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis

               // puxa os registros no pai
               for(int j = diminuido-1; j < pa.n; j++)
               {
                  pa.chaves[j]   = pa.chaves[j+1];
                  pa.dados[j]    = pa.dados[j+1];
                  pa.filhos[j+1] = pa.filhos[j+2];
               }// end for

               pa.n--;
               diminuiu = pa.n < ordem;  // testa se o pai também ficou sem o número mínimo de elementos
            }// end if
         }
         else // fusão com o irmão direito
         {
            paginaIrmao = pa.filhos[diminuido+1];
            pIrmao = new ArvoreB_Pagina(ordem);
            arquivo.seek(paginaIrmao);
            arquivo.read(buffer);
            pIrmao.setBytes(buffer);

            // Testa se o irmão pode emprestar algum registro
            if(pIrmao.n > ordem)
            {
               // desce o elemento pai
               pFilho.chaves[pFilho.n]   = pa.chaves[diminuido];
               pFilho.dados[pFilho.n]    = pa.dados[diminuido];
               pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
               pFilho.n++;

               // sobe o elemento do irmão
               pa.chaves[diminuido] = pIrmao.chaves[0];
               pa.dados[diminuido]  = pIrmao.dados[0];

               // move todos os registros no irmão para a esquerda
               int j;
               for(j = 0; j < pIrmao.n-1; j++)
               {
                  pIrmao.chaves[j] = pIrmao.chaves[j+1];
                  pIrmao.dados[j]  = pIrmao.dados[j+1];
                  pIrmao.filhos[j] = pIrmao.filhos[j+1];
               }// end for

               pIrmao.filhos[j] = pIrmao.filhos[j+1];
               pIrmao.n--;
               diminuiu = false;
            }
            else
            {
               // Desce o registro no pai para o filho
               pFilho.chaves[pFilho.n]   = pa.chaves[diminuido];
               pFilho.dados[pFilho.n]    = pa.dados[diminuido];
               pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
               pFilho.n++;

               // copia todos os registros do irmão da direita
               for(int j = 0; j < pIrmao.n; j++)
               {
                  pFilho.chaves[pFilho.n]   = pIrmao.chaves[j];
                  pFilho.dados[pFilho.n]    = pIrmao.dados[j];
                  pFilho.filhos[pFilho.n+1] = pIrmao.filhos[j+1];
                  pFilho.n++;
               }// end for

               pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis

               // puxa os registros no pai
               for(int j = diminuido; j < pa.n-1; j++)
               {
                  pa.chaves[j]   = pa.chaves[j+1];
                  pa.dados[j]    = pa.dados[j+1];
                  pa.filhos[j+1] = pa.filhos[j+2];
               }// end for

               pa.n--;
               diminuiu = pa.n < ordem;  // testa se o pai também ficou sem o número mínimo de elementos
            }// end if
         }// end if

         // Atualiza todos os registros
         arquivo.seek(pagina);
         arquivo.write(pa.getBytes());
         arquivo.seek(paginaFilho);
         arquivo.write(pFilho.getBytes());
         arquivo.seek(paginaIrmao);
         arquivo.write(pIrmao.getBytes());
      }// end if

      return excluido;
   }// end excluir1( )

   public void print( ) throws IOException
   {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong( );

      if(raiz != -1)
         print1(raiz,0,0);
   }// end print( )

   private void print1(long pagina, int nivel, int dados) throws IOException
   {
      if(pagina == -1)
         return;

      int i;

      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);

      System.out.print(nivel + "." + dados + ": (");

      for(i = 0; i < pa.n; i++)
      {
         if(pa.filhos[i] != -1)
            System.out.print((nivel+1) + "." + ((nivel*dados*ordem*2)+i));

         System.out.print(") " + pa.chaves[i] + " (");
      }// end for

      if(pa.filhos[i] == -1)
         System.out.println(")");
      else
         System.out.println((nivel + 1) + "." + ((nivel*dados*ordem*2) + i) + ")");

      for(i = 0; i <= pa.n; i++)
         print1(pa.filhos[i],nivel+1,((nivel*dados*ordem*2)+i));
   }// end print1( )

   public void apagar( ) throws IOException
   {
      File f = new File(nomeArquivo);
      f.delete( );

      arquivo = new RandomAccessFile(nomeArquivo,"rw");
      arquivo.writeLong(-1);  // raiz vazia
   }// end apagar( )
}// end class ArvoreB_Indireto