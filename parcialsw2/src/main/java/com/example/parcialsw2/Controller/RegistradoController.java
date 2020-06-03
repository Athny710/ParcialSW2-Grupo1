package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.ProdSelRepository;
import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/registrado")
public class RegistradoController {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    ProdSelRepository prodSelRepository;

    @GetMapping(value = {"","/list"})
    public String paginaInicio(Model model){
        model.addAttribute("lista", productoRepository.findAll());
        return "index";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Producto p = opt.get();
            byte[] imagenComoBytes = p.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(p.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        }else{
            return null;
        }
    }
    @GetMapping("/carrito")
    public String añadirCarrito(@RequestParam("id") int id, HttpSession session, RedirectAttributes attr){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Producto p = opt.get();
            Usuario usuario = (Usuario) session.getAttribute("user");
            List<ProductoSel> seleccionado = prodSelRepository.findByCarrito(p.getIdproducto(),usuario.getIdusuarios());
            if(seleccionado.isEmpty()){
                ProductoSel productoSel= new ProductoSel();
                productoSel.setUsuario(usuario);
                productoSel.setProducto(p);
                productoSel.setCantidad(1);
                productoSel.setComprado(0);
                attr.addFlashAttribute("msg","Se agregó correctamente");
                int cantidadCarrito = (int) session.getAttribute("numeroCarrito");
                cantidadCarrito = cantidadCarrito +1;
                session.setAttribute("numeroCarrito", cantidadCarrito);

                prodSelRepository.save(productoSel);
            }else{
                for (ProductoSel pro : seleccionado) {
                   if(pro.getProducto().getIdproducto() == p.getIdproducto()) {
                       int cantidad = 1 + pro.getCantidad();
                       if (cantidad > 4) {
                           attr.addFlashAttribute("msg1", "Solo se permiten 4 unidades por producto");
                           return "redirect:/list";
                       } else {
                           pro.setCantidad(cantidad);
                           prodSelRepository.save(pro);
                           int cantidadCarrito = (int) session.getAttribute("numeroCarrito");
                           cantidadCarrito = cantidadCarrito +1;
                           session.setAttribute("numeroCarrito", cantidadCarrito);
                           attr.addFlashAttribute("msg", "Se agregó correctamente");
                           break;
                       }
                   }
                }
            }
            return "redirect:/list";
        }else{
            return "redirect:/list";
        }
    }

    @GetMapping("/verCarrito")
    public String verCarrito(Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("user");
        List<ProductoSel> carrito = prodSelRepository.NumeroCarrito(usuario.getIdusuarios());
        double precioTotal = 0;
        for(ProductoSel pro : carrito){
            precioTotal = precioTotal + (pro.getCantidad() * pro.getProducto().getPrecio());
        }

        model.addAttribute("precioFinal", precioTotal);
        model.addAttribute("lista",carrito);
        return "registrado/carrito";
    }

    @GetMapping("/vistacheck")
    public String vistacheck(){
        return "registrado/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("numero") String num, Model model,RedirectAttributes attr, HttpSession session){
        if(verificarTarjeta(num)){
            if(getCCType(num).equals("VISA")){
                Usuario usuario = (Usuario) session.getAttribute("user");
                List<ProductoSel> carrito = prodSelRepository.NumeroCarrito(usuario.getIdusuarios());
                List<ProductoSel> paraid = prodSelRepository.findAll();
                Calendar fecha = new GregorianCalendar();
                int año = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH);
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                String fechaActual = dia + "" + (mes+1) + "" +año ;
                int auto = paraid.size();
                String codigoGenerado = "PE" + fechaActual + auto;
                for(ProductoSel pro : carrito){
                    pro.setComprado(1);
                    pro.setCodigo(codigoGenerado);
                    prodSelRepository.save(pro);
                }
                attr.addFlashAttribute("msg2", "Compra realizada exitosamente con tarjeta VISA");
                int cantidadCarrito = 0;
                session.setAttribute("numeroCarrito", cantidadCarrito);
                return "redirect:/registrado";
            }else if (getCCType(num).equals("MASTER")) {

                attr.addFlashAttribute("msg2", "Compra realizada exitosamente con tarjeta MASTERCARD");
                return "redirect:/registrado";
            }else{
                model.addAttribute("msg", "su tarjeta es valida, pero no es VISA ni MASTERCARD");
                return "registrado/checkout";
            }
        }else{
            model.addAttribute("msg", "tarjeta invalida");
            return "registrado/checkout";
        }

    }

    public boolean verificarTarjeta(String numero){
        char[] caracteres = numero.toCharArray();
        int[] carac2 = new int[15];
        int ultimo = Character.getNumericValue(caracteres[15]);
        int suma=0;
        for(int i=0, j=14; i<15; i++, j--){
            carac2[i] = Character.getNumericValue(caracteres[j]);
        }
        for (int i=0; i<15; i++){
            if(i%2==0){ carac2[i]=carac2[i]*2; }
        }
        for (int i=0; i<15; i++){
            if(carac2[i]>9){
                carac2[i]=carac2[i]-9;
            }
        }
        for (int i=0; i<15;i++){
            System.out.println(carac2[i]);
        }
        for (int i=0; i<15; i++){
            suma= suma + carac2[i];
        }
        System.out.println(suma);
        if(10-(suma%10)==ultimo){
            return true;
        }else if(suma%10 == ultimo){
            return true;
        }else{
            return false;
        }
    }
    public String getCCType(String ccNumber){
        String visaRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex = "^5[1-5][0-9]{14}$";
        //String amexRegex = "^3[47][0-9]{13}$";
        //String dinersClubrRegex = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        //String discoverRegex = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        //String jcbRegex = "^(?:2131|1800|35\\d{3})\\d{11}$";
        //String commonRegex = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
        try { ccNumber = ccNumber.replaceAll("\\D", "");
            return (ccNumber.matches(visaRegex) ? "VISA" : ccNumber.matches(masterRegex) ? "MASTER" :null);
        } catch (Exception e) { e.printStackTrace(); }
        return null; }

    @GetMapping("/listapedidos")
    public String listapedidos(Model model){
       // model.addAttribute("listapedidos",prodSelRepository.findById());
        return "";
    }

        @GetMapping("/quitarCarrito")
        public String quitarCarrito(@RequestParam("id") int id, HttpSession session,RedirectAttributes attr){
        Optional<Producto> pro1 = productoRepository.findById(id);
        if (pro1.isPresent()){
            Producto p = pro1.get();
            Usuario usuario = (Usuario) session.getAttribute("user");
            List<ProductoSel> seleccionado = prodSelRepository.findByCarrito(p.getIdproducto(),usuario.getIdusuarios());
            if (seleccionado.isEmpty()){
                return "redirect:/registrado/verCarrito";
            }else{
                for (ProductoSel pro : seleccionado) {
                    if(pro.getProducto().getIdproducto() == p.getIdproducto()) {
                        int cantidad = pro.getCantidad() - 1;
                        if (cantidad <=  0) {
                            attr.addFlashAttribute("msg1", "Producto Borrado");
                            int cantidadCarrito = (int) session.getAttribute("numeroCarrito");
                            cantidadCarrito = cantidadCarrito -1;
                            session.setAttribute("numeroCarrito", cantidadCarrito);
                            prodSelRepository.deleteById(pro.getIdproductoseleccionado());
                            return "redirect:/registrado/verCarrito";
                        } else {
                            pro.setCantidad(cantidad);
                            prodSelRepository.save(pro);
                            int cantidadCarrito = (int) session.getAttribute("numeroCarrito");
                            cantidadCarrito = cantidadCarrito -1;
                            session.setAttribute("numeroCarrito", cantidadCarrito);
                            attr.addFlashAttribute("msg", "Quitaste un Producto");
                            break;
                        }
                    }
                }
            }
            return "redirect:/registrado/verCarrito";
        }else {
            return "redirect:/registrado/verCarrito";
        }


        }

}
