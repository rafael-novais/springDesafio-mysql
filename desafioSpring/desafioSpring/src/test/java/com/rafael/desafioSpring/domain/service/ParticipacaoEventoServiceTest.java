package com.rafael.desafioSpring.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rafael.desafioSpring.domain.dto.request.FlagCreateRequest;
import com.rafael.desafioSpring.domain.entities.Evento;
import com.rafael.desafioSpring.domain.entities.Participacao;
import com.rafael.desafioSpring.domain.entities.StatusEvento;
import com.rafael.desafioSpring.repository.ParticipacaoRepository;
import com.rafael.desafioSpring.service.EventoService;
import com.rafael.desafioSpring.service.ParticipacaoService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParticipacaoEventoServiceTest {

    //usar inject mock dá uns bugs estranhos ao usar WHEN, ele entra no metodo de fato "ignorando o when"
    @InjectMocks
    public ParticipacaoService service;

    //fiz esse service com mock simples pra testar
    @Mock
    public ParticipacaoService serviceTeste;

    @Mock
    public ParticipacaoRepository repository;

    @Mock
    public EventoService eventoService;

    public Participacao participacao = new Participacao();
    public Evento evento = new Evento();
    public StatusEvento statusEvento = new StatusEvento();

    public ParticipacaoEventoServiceTest(){

        this.evento.setIdEvento(1);
        this.participacao.setIdEvento(evento);
        this.participacao.setComentario("Comentario");
        this.participacao.setFlagPresente(true);
        this.participacao.setIdEvento(evento);
        this.participacao.setIdParticipacao(1);
        this.participacao.setLoginParticipante("loginParticipante");
        this.participacao.setNota(2);

    }

    @Test
    public void should_create(){

        when(repository.save(any())).thenReturn(this.participacao);
        
        Participacao retorno = service.createParticipacao(this.participacao);

        // then
        assertEquals(retorno, this.participacao);

    }

    @Test
    public void should_listParticipacao(){

        List<Participacao> list = new ArrayList<>();
        list.add(this.participacao);

        // given
        when(repository.findAll()).thenReturn(list);

        // when
        List<Participacao> model = service.listParticipacao();

        // then
        assertEquals(list, model);

    }

    @Test
    public void should_inscrever(){

        //sets para passar na 1ª condição
        this.evento.setIdEventoStatus(this.statusEvento);
        this.statusEvento.setIdEventoStatus(1);
        when(eventoService.findById(any())).thenReturn(this.evento);

        //mock da 2ª condição
        when(eventoService.buscaPorVagasDisponiveis(any())).thenReturn(2);

        //sets para passar na 3ª condição
        this.participacao.setLoginParticipante("sgssgdg");
        Participacao testeP = new Participacao();
        testeP.setLoginParticipante("GasdaFaEl");
        List<Participacao> list = new ArrayList<>();
        list.add(testeP);
        when(repository.listParticipacaoPorEvento(any())).thenReturn(list);

        when(service.createParticipacao(this.participacao)).thenReturn(this.participacao);

        Participacao retorno = service.inscrever(this.participacao);

        // then
        assertEquals(retorno, this.participacao);

    }

    @Test
    public void should_validarStatusEvento(){

        when(eventoService.findById(anyInt())).thenReturn(this.evento);
        
        this.evento.setIdEventoStatus(this.statusEvento);

        //teste 1ª condição
        this.statusEvento.setIdEventoStatus(1);
        Boolean teste1 = service.validarStatusEvento(1);

        //teste 2ª condição
        this.statusEvento.setIdEventoStatus(2);
        Boolean teste2 = service.validarStatusEvento(1);

        //teste 3ª condição
        this.statusEvento.setIdEventoStatus(3);
        Boolean teste3 = service.validarStatusEvento(1);

        //teste 4ª condição
        this.statusEvento.setIdEventoStatus(4);
        Boolean teste4 = service.validarStatusEvento(1);


        // then
        assertEquals(teste1, true);
        assertEquals(teste2, false);
        assertEquals(teste3, false);
        assertEquals(teste4, false);

    }

    @Test
    public void should_validarDuplicidadeDeInscricao(){

        this.participacao.setLoginParticipante("raFaEl");
        List<Participacao> list = new ArrayList<>();
        list.add(this.participacao);
        when(service.buscarInscritosNoEvento(anyInt())).thenReturn(list);

        Boolean teste = service.validarDuplicidadeDeInscricao("RafaeL", 2);
        Boolean teste2 = service.validarDuplicidadeDeInscricao("gabriel", 2);

        assertEquals(teste, true);
        assertEquals(teste2, false);
    }

    @Test
    public void should_buscarInscritosNoEvento(){

        List<Participacao> list = new ArrayList<>();
        list.add(this.participacao);

        when(repository.listParticipacaoPorEvento(anyInt())).thenReturn(list);

        List<Participacao> participacaoList = service.buscarInscritosNoEvento(1); 
        
        assertEquals(participacaoList, list);

    }

    @Test
    public void should_avaliar(){

        when(repository.findParticipacaoByIdParticipacao(anyInt())).thenReturn(this.participacao);
        when(repository.save(any())).thenReturn(this.participacao);

        Participacao teste = service.avaliar(1, this.participacao);
        
        assertEquals(teste, this.participacao);

    }

    @Test
    public void should_salvar(){

        when(repository.findById(anyInt())).thenReturn(Optional.of(this.participacao));
        when(repository.save(any())).thenReturn(this.participacao);

        FlagCreateRequest flag = new FlagCreateRequest();
        flag.setFlag(true);

        Participacao teste = service.salvar(flag, 1);
        
        assertEquals(teste, this.participacao);

    }

    @Test
    public void should_findById(){

        when(repository.findById(anyInt())).thenReturn(Optional.of(this.participacao));
    
        Participacao teste = service.findById(1);
        
        assertEquals(teste, this.participacao);

    }

    @Test
    public void should_buscaPorLogin(){

        List<Participacao> list = new ArrayList<>();
        list.add(this.participacao);        

        when(repository.listByLogin(any())).thenReturn(list);
    
        List<Participacao> teste = service.buscaPorLogin("teste");
        
        assertEquals(teste, list);

    }

    @Test
    public void should_updateParticipacao(){

        when(repository.findById(anyInt())).thenReturn(Optional.of(this.participacao));
        when(repository.save(any())).thenReturn(this.participacao);

        Participacao teste = service.updateParticipacao(1, this.participacao);
        
        assertEquals(teste, this.participacao);

    }

}