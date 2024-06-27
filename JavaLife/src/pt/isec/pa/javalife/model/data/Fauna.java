package pt.isec.pa.javalife.model.data;

import pt.isec.pa.javalife.model.fsm.JavaLifeState;
import pt.isec.pa.javalife.model.fsm.states.IJavaLifeState;

import java.io.Serializable;
import java.util.Set;

public final class Fauna extends ElementoBase implements IElementoComForca, Serializable, IElementoComImagem {
    private double forca;
    private static int idFauna = 0;
    private final int id;
    private Set<IElemento> elementos;
    Area area;
    private double defaultSpeed;
    private double solValue;
    private int secondsToReproduce;
    private String imagem;

    private double perdaForcaMovimento;
    private double ganhaForcaAlimento;

    public Fauna(Area area,Set<IElemento> elementos, double forcaDefault, double perdaForcaMovimento, double ganhaForcaAlimento) {
        this.area = area;
        this.id = idFauna++;
        this.elementos = elementos;

        this.forca = forcaDefault;
        this.perdaForcaMovimento = perdaForcaMovimento;
        this.ganhaForcaAlimento = ganhaForcaAlimento;

        this.currentState = JavaLifeState.MOVING.getInstance(this);
        this.defaultSpeed = 5;
        this.solValue = defaultSpeed /2;
        this.secondsToReproduce = 0;
    }

    /*public Fauna(Area area,Set<IElemento> elementos, double forca) {
        this.area = area;
        this.id = idFauna++;
        this.elementos = elementos;
        this.forca = forca;
        this.currentState = JavaLifeState.MOVING.getInstance(this);
        this.defaultSpeed = 5;
        this.solValue = defaultSpeed /2;
        this.secondsToReproduce = 0;
    }*/

    @Override
    public double getForca() {
        return forca;
    }

    @Override
    public void setForca(double forca) {
        if (forca <= 100) {
            this.forca = forca;
        }
        else
            this.forca = 100;
    }

    public void setGanhoForca(double forca) {
        this.ganhaForcaAlimento = forca;
    }

    public void setPerdaForcaMovimento(double forca) {
        this.perdaForcaMovimento = forca;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Elemento getType() {
        return Elemento.FAUNA;
    }

    @Override
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public double getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(double defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public double getSolValue() {
        return solValue;
    }

    public int getSecondsToReproduce() {
        return secondsToReproduce;
    }

    public void setSecondsToReproduce(int secondsToReproduce) {
        this.secondsToReproduce = secondsToReproduce;
    }

    @Override
    public void evolve() {
        currentState.evolve();
    }
    public void atualizaMapa(Set<IElemento> elementos){
        this.elementos = elementos;
    }



    //ESTADOS
    private IJavaLifeState currentState;

    public void changeState(IJavaLifeState newState) {
        currentState = newState;
    }

    public JavaLifeState getState() {
        return currentState.getState();
    }

    public Set<IElemento> getElementos() {
        return elementos;
    }

    public void suiced(){
        elementos.remove((this));
    }

    public void diminiuForca(){
        forca-= perdaForcaMovimento;
    }

    public void alimentar() {
        if(forca < 100){
            forca += ganhaForcaAlimento;
        }
    }

    @Override
    public String getImagem() {
        return imagem;
    }

    @Override
    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


    public double getPerdaForcaMovimento(){
        return perdaForcaMovimento;
    }
    public double getGanhaForcaAlimento(){
        return ganhaForcaAlimento;
    }
}
