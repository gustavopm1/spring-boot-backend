package com.gpmoraes.desafio.services;

import com.gpmoraes.desafio.domain.ItemPedido;
import com.gpmoraes.desafio.domain.Pagamento;
import com.gpmoraes.desafio.domain.PagamentoComBoleto;
import com.gpmoraes.desafio.domain.Pedido;
import com.gpmoraes.desafio.domain.enums.EstadoPagamento;
import com.gpmoraes.desafio.repositories.ItemPedidoRepository;
import com.gpmoraes.desafio.repositories.PagamentoRepository;
import com.gpmoraes.desafio.repositories.PedidoRepository;
import com.gpmoraes.desafio.repositories.ProdutoRepository;
import com.gpmoraes.desafio.services.Exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Pedido find(Integer id){
        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
    }

    public Pedido insert(Pedido obj){
        obj.setId(null);
        obj.setInstante(new Date());
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if(obj.getPagamento() instanceof PagamentoComBoleto){
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
        }
        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for(ItemPedido ip : obj.getItens()){
            ip.setDesconto(0.0);
            ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
            ip.setPedido(obj);;

        }
        itemPedidoRepository.saveAll(obj.getItens());
        return obj;
    }
}
