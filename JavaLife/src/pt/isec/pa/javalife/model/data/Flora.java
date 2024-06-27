package pt.isec.pa.javalife.model.data;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

public final class Flora extends ElementoBase implements IElementoComImagem, IElementoComForca, Serializable {
    private double forca;
    private static int idFlora = 0;
    private final int id;
    Area area;
    private String imagem;
    private Set<IElemento> elementos;
    private int contadorReproducao;
    private double defaultIncrement;
    private double solValue;

    private boolean morrer;
    private double perdaForca;


    public Flora(Area area, double defaultForca, double perdaForca) {
        this.area = area;
        this.id = idFlora++;
        this.forca = defaultForca;
        this.perdaForca = perdaForca;
        this.contadorReproducao = 0;
        this.defaultIncrement = 0.5;
        this.solValue = defaultIncrement * 2;
        this.morrer = false;
    }
    @Override
    public double getForca() {
        return forca;
    }

    @Override
    public void setForca(double forca) {
        this.forca = forca;
    }
    public void setPerdaForca(double perdaForca){
        this.perdaForca = perdaForca;
    }
    @Override
    public String getImagem() {
        return imagem;
    }

    @Override
    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.FLORA;
    }

    @Override
    public Area getArea() {
        return area;
    }

    public int getContadorReproducao() {
        return contadorReproducao;
    }

    public void setContadorReproducao(int contadorReproducao) {
        this.contadorReproducao = contadorReproducao;
    }

    @Override
    public void evolve() {
        if(morrer){
            forca -= perdaForca;
            if(forca <= 0){
                elementos.remove(this);
            }
        }else {
            if (forca < 100) {
                forca += defaultIncrement;
            }
        }
        morrer = false;
        atualizaMapa(elementos);
    }

    public void atualizaMapa(Set<IElemento> elementos){
        this.elementos = elementos;
    }


    public double getDefaultIncrement() {
        return defaultIncrement;
    }

    public void setDefaultIncrement(double defaultIncrement) {
        this.defaultIncrement = defaultIncrement;
    }

    public double getSolValue() {
        return solValue;
    }

    public void setMorrer(boolean morrer) {
        this.morrer = morrer;
    }


    public double getPerdaForca(){
        return perdaForca;
    }
}
