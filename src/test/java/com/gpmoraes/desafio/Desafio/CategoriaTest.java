package com.gpmoraes.desafio.Desafio;

import com.gpmoraes.desafio.domain.Categoria;
import com.gpmoraes.desafio.domain.Produto;
import com.gpmoraes.desafio.repositories.CategoriaRepository;
import com.gpmoraes.desafio.services.CategoriaService;
import com.gpmoraes.desafio.services.exceptions.DataIntegrityException;
import com.gpmoraes.desafio.services.exceptions.ObjectNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoriaTest {

    @Mock
    CategoriaRepository categoriaRepository;

    @Spy
    @InjectMocks
    CategoriaService categoriaService;


    @Test
    public void testGetCaregoriaByID() {


        Optional<Categoria> categoriaFake = Optional.of(Categoria.builder()
                .id(1)
                .nome("test")
                .build());

        List<Categoria> categorias = Collections.singletonList(categoriaFake.get());

        Produto produto1 = Produto.builder().id(1).nome("prod1").categorias(categorias).build();
        Produto produto2 = Produto.builder().id(2).nome("prod2").categorias(categorias).build();

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        doReturn(categoriaFake).when(categoriaRepository).findById(any(Integer.class));

        Categoria categoria = categoriaService.find(1);

        assertEquals(Long.valueOf(1), Long.valueOf(categoriaFake.get().getId()));
        assertEquals(Long.valueOf(1), Long.valueOf(produto1.getId()));
        assertEquals(Long.valueOf(2), Long.valueOf(produto2.getId()));

        verify(categoriaRepository, times(1)).findById(1);

    }

    @Test
    public void testPutCategoria(){

        Optional<Categoria> categoriaFake = Optional.of(Categoria.builder()
                .id(1)
                .nome("test")
                .build());

        Categoria categoriaFakeUpdated = (Categoria.builder()
            .id(1)
            .nome("Test2")
            .build());

        doReturn(categoriaFake).when(categoriaRepository).findById(any(Integer.class));
        doReturn(categoriaFakeUpdated).when(categoriaRepository).save(any(Categoria.class));

        Categoria categoria = categoriaService.update(categoriaFakeUpdated);

        verify(categoriaRepository, times(1)).save(any());

        assertEquals("Test2", categoria.getNome());
        assertEquals(Long.valueOf(1), Long.valueOf(categoria.getId()));

    }

    @Test
    public void testPostCategoria(){
        Categoria categoriaFake = Categoria.builder()
                .id(1)
                .nome("test")
                .build();

        doReturn(categoriaFake).when(categoriaRepository).save(any(Categoria.class));

        Categoria categoria = categoriaService.insert(categoriaFake);

        verify(categoriaRepository, times(1)).save(any());

        categoria.setId(1);

        assertEquals("test",categoriaFake.getNome());
        assertEquals(Long.valueOf(1),Long.valueOf(categoriaFake.getId()));
    }


    @Test
    public void testDeleteCategoria(){
        Optional<Categoria> categoriaFake = Optional.of(Categoria.builder()
                .id(1)
                .nome("test")
                .build());


        doReturn(categoriaFake).when(categoriaRepository).findById(any(Integer.class));


        categoriaService.delete(1);
        verify(categoriaRepository,times(1)).deleteById(1);
    }

    @Test(expected = DataIntegrityException.class)
    public void testDeletCategoriaComProdutos(){

        Categoria categoriaFake = Categoria.builder()
                .id(1)
                .nome("test")
                .build();

        List<Categoria> categorias = Collections.singletonList(categoriaFake);

        Produto produto1 = Produto.builder().id(1).nome("prod1").categorias(categorias).preco(33.0).build();
        Produto produto2 = Produto.builder().id(2).nome("prod2").categorias(categorias).preco(50.0).build();

        categoriaFake.setProdutos(Arrays.asList(produto1, produto2));

        doReturn(Optional.of(categoriaFake)).when(categoriaRepository).findById(any(Integer.class));

        doThrow(DataIntegrityViolationException.class).when(categoriaRepository).deleteById(anyInt());

        categoriaService.delete(1);

        verify(categoriaRepository,times(1)).deleteById(1);


    }


    @Test(expected = ObjectNotFoundException.class)
    public void testGetCategoriaInexistente(){
        Optional<Categoria> categoriaFake = Optional.of(Categoria.builder()
                .id(1)
                .nome("test")
                .build());

        List<Categoria> categorias = Collections.singletonList(categoriaFake.get());

        Produto produto1 = Produto.builder().id(1).nome("prod1").categorias(categorias).build();
        Produto produto2 = Produto.builder().id(2).nome("prod2").categorias(categorias).build();

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        categoriaService.find(1);

        verify(categoriaRepository, times(1)).findById(1);
    }



}
