package pt.isec.pa.javalife.model.data;

public sealed interface IElementoComImagem permits Flora,Fauna {
    String getImagem(); // path da imagem
    void setImagem(String imagem);
}
