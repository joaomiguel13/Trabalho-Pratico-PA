package pt.isec.pa.javalife.model.fsm.states;

import pt.isec.pa.javalife.model.data.*;
import pt.isec.pa.javalife.model.fsm.JavaLifeState;
import pt.isec.pa.javalife.model.fsm.JavaLifeStateAdapter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SearchingFoodState extends JavaLifeStateAdapter {
    public SearchingFoodState(Fauna data) {
        super(data);
    }

    public JavaLifeState getState(){
        return JavaLifeState.SEARCHING_FOOD;
    }

    @Override
    public void evolve(){
        boolean done = false;
        boolean colide = false;
        boolean colideFlora = false;
        Set<IElemento> copyOfElementos = new HashSet<>(data.getElementos());
        IElemento faunaProxima;
        IElemento floraMaisProxima ;
        while (!done) {
            Random random = new Random();
            faunaProxima = null;
            floraMaisProxima = null;
            if(data.getForca() >= 80){
                data.changeState(new MovingState(data));
                done = true;
            }
            //ver se esta a comer
            for (IElemento e : copyOfElementos) {
                if (e.getArea().intersects(data.getArea()) && e.getId() != data.getId()) {
                    if (e.getType()== Elemento.FLORA && data.getForca() < 100){
                        //FICAR PARA E ROUBAR FORÇA
                        data.alimentar();
                        ((Flora) e).setMorrer(true);
                        //((Flora) e).comida();
                        colideFlora = true;
                        break;
                    }
                }
                done = true;
            }
            if(!colideFlora){
                double menorDistancia =Double.MAX_VALUE;

                for (IElemento e : copyOfElementos) {
                    if (e.getType()== Elemento.FLORA) {
                        Area areaOutro = e.getArea();
                        Area minhaArea = data.getArea();

                        double distancia = Area.calcularMenorDistancia(minhaArea, areaOutro);
                        if (distancia < menorDistancia) {
                            menorDistancia = distancia;
                            floraMaisProxima = e;
                        }
                    }
                }

                if (floraMaisProxima != null) {
                    Area areaFlora = floraMaisProxima.getArea();
                    Area minhaArea = data.getArea();


                    double centroXMinha = (minhaArea.esquerda() + minhaArea.direita()) / 2;
                    double centroYMinha = (minhaArea.cima() + minhaArea.baixo()) / 2;
                    double centroXFlora = (areaFlora.esquerda() + areaFlora.direita()) / 2;
                    double centroYFlora = (areaFlora.cima() + areaFlora.baixo()) / 2;

                    int direction = -1;
                    if (Math.abs(centroXFlora - centroXMinha) > Math.abs(centroYFlora - centroYMinha)) {
                        if (centroXFlora > centroXMinha) {
                            direction = 0; // Direita
                        } else {
                            direction = 3; // Esquerda
                        }
                    } else {
                        if (centroYFlora > centroYMinha) {
                            direction = 2; // Baixo
                        } else {
                            direction = 1; // Cima
                        }
                    }

                    switch (direction) {

                        case 0 -> { // Direita
                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId()) {
                                    if(e.getType()== Elemento.FLORA){
                                        colideFlora = true;
                                        break;
                                    }else if(e.getType()== Elemento.INANIMADO){
                                        colide = true;
                                        break;
                                    }

                                }
                            }
                            if (!colide) {

                                    data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5));
                                    data.diminiuForca();
                                    done = true;

                            }

                        }
                        case 1 -> { // Cima
                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId()) {
                                    if(e.getType()== Elemento.FLORA){
                                        colideFlora = true;
                                        break;
                                    }else if(e.getType()== Elemento.INANIMADO){
                                        colide = true;
                                        break;
                                    }
                                }
                            }
                            if (!colide){

                                    data.setArea(new Area(data.getArea().cima() - 5, data.getArea().esquerda(), data.getArea().baixo() - 5, data.getArea().direita()));
                                    data.diminiuForca();
                                    done = true;

                            }

                        }
                        case 2 -> { // Baixo
                            for (IElemento e : copyOfElementos) {
                                if(e.getType()== Elemento.FLORA){
                                    colideFlora = true;
                                    break;
                                }else if(e.getType()== Elemento.INANIMADO){
                                    colide = true;
                                    break;
                                }
                            }
                            if (!colide) {

                                    data.setArea(new Area(data.getArea().cima() + 5, data.getArea().esquerda(), data.getArea().baixo() + 5, data.getArea().direita()));
                                    data.diminiuForca();
                                    done = true;

                            }

                        }
                        case 3 -> { // Esquerda
                            for (IElemento e : copyOfElementos) {
                                if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId()) {
                                    if(e.getType()== Elemento.FLORA){
                                        colideFlora = true;
                                        break;
                                    }else if(e.getType()== Elemento.INANIMADO){
                                        colide = true;
                                        break;
                                    }
                                }
                            }
                            if (!colide) {

                                    data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - 5, data.getArea().baixo(), data.getArea().direita() - 5));
                                    data.diminiuForca();
                                    done = true;


                            }
                        }

                    }

                    if (data.getForca() <= 0) {
                        data.suiced();
                    }
                }else{//nao existe flora

                    double menorForca =Double.MAX_VALUE;
                    if(faunaProxima == null){
                        for (IElemento e : copyOfElementos) {
                            if (e instanceof Fauna && e.getId()!= data.getId()) {
                                if (((Fauna) e).getForca() < menorForca) {
                                    menorForca = ((Fauna) e).getForca();
                                    faunaProxima = e;
                                }
                            }
                        }
                    }

                    if(faunaProxima != null){
                        Area areaFauna = faunaProxima.getArea();
                        Area minhaArea = data.getArea();
                        //verificar se o meu target esta adjacente

                        if (minhaArea.intersects(areaFauna) && faunaProxima.getId() != data.getId()){

                            //ver se estão os dois no mesmo estad
                            if(((Fauna) faunaProxima).getState() == JavaLifeState.SEARCHING_FOOD){

                                //se estiverem no mesmo estado
                                //ver se a minha força é maior
                                if(data.getForca() > ((Fauna) faunaProxima).getForca() && data.getForca() > 10){

                                    if(data.getForca()+((Fauna) faunaProxima).getForca() > 100){
                                        data.setForca(100);
                                    }
                                    else{
                                        data.setForca(data.getForca()+((Fauna) faunaProxima).getForca());
                                    }
                                    data.setForca(data.getForca()-10);
                                    ((Fauna) faunaProxima).suiced();
                                    //roubar a força
                                }
                            }
                            done=true;
                        }

                        //tem de ir para a mais proxima


                        // Determine a direção a partir das coordenadas
                        double centroXMinha = (minhaArea.esquerda() + minhaArea.direita()) / 2;
                        double centroYMinha = (minhaArea.cima() + minhaArea.baixo()) / 2;
                        double centroXFlora = (areaFauna.esquerda() + areaFauna.direita()) / 2;
                        double centroYFlora = (areaFauna.cima() + areaFauna.baixo()) / 2;

                        int direction = -1;
                        if (Math.abs(centroXFlora - centroXMinha) > Math.abs(centroYFlora - centroYMinha)) {
                            if (centroXFlora > centroXMinha) {
                                direction = 0; // Direita
                            } else {
                                direction = 3; // Esquerda
                            }
                        } else {
                            if (centroYFlora > centroYMinha) {
                                direction = 2; // Baixo
                            } else {
                                direction = 1; // Cima
                            }
                        }

                        switch (direction) {

                            case 0 -> { // Direita
                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {

                                        data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5));
                                        data.diminiuForca();
                                        done = true;

                                }

                            }
                            case 1 -> { // Cima
                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide){

                                        data.setArea(new Area(data.getArea().cima() - 5, data.getArea().esquerda(), data.getArea().baixo() - 5, data.getArea().direita()));
                                        data.diminiuForca();
                                        done = true;

                                }

                            }
                            case 2 -> { // Baixo
                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {

                                        data.setArea(new Area(data.getArea().cima() + 5, data.getArea().esquerda(), data.getArea().baixo() + 5, data.getArea().direita()));
                                        data.diminiuForca();
                                        done = true;

                                }

                            }
                            case 3 -> { // Esquerda
                                for (IElemento e : copyOfElementos) {
                                    if (e.getArea().intersects(new Area(data.getArea().cima(), data.getArea().esquerda() + 5, data.getArea().baixo(), data.getArea().direita() + 5)) && e.getId() != data.getId() && e.getType()== Elemento.INANIMADO) {

                                        colide = true;
                                        break;
                                    }
                                }
                                if (!colide) {

                                        data.setArea(new Area(data.getArea().cima(), data.getArea().esquerda() - 5, data.getArea().baixo(), data.getArea().direita() - 5));
                                        data.diminiuForca();
                                        done = true;


                                }
                            }
                        }

                        if (data.getForca() <= 0) {
                            data.changeState(new DeadState(data));
                        }

                    }

                }


            }
        }
    }
}