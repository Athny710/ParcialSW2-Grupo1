package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.UsuarioRepository;
import com.example.parcialsw2.service.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Max;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Service
public class HomeController {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private JavaMailSender sender;

    @RequestMapping("/enviarCorreo")
    @ResponseBody
    String home() {
        try {
            sendEmail();
            return "Email Sent!";
        }catch(Exception ex) {
            return "Error in sending email: "+ex;
        }
    }

    private void sendEmail() throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("mionks27@gmailcom");
        helper.setText("How are you?");
        helper.setSubject("Hi");

        sender.send(message);
    }







    @GetMapping(value = {"","/list"})
    public String index(Model model){
        model.addAttribute("lista", productoRepository.findAll());
        return "index2";

    }
    @GetMapping("/vermas")
    public String vermas(@RequestParam("id") int id, Model model, HttpSession session){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Usuario usuario = (Usuario) session.getAttribute("user");
            if (usuario == null){
                model.addAttribute("producto", opt.get());
                return "system/Detalles2";

            }
            model.addAttribute("producto", opt.get());
            return "system/Detalles";
        }else{
            return "redirect:/list";
        }
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

    @GetMapping("registrarse")
    public String registrarse(@ModelAttribute("usuario") Usuario u){
        return "system/Registrarse";
    }




    @GetMapping("recuperar")
    public String recuperarContra(){
        return "system/RecuperarCont";
    }



    @PostMapping("/cambiar")
    public String cambiar(@RequestParam("pss1") String pss1,
                          @RequestParam("pss2") String pss2,
                          @RequestParam("correo") String correo, Model model){

        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if(pss1.equals(pss2)){

            if(usuario!=null){
                usuario.setContrasenha(new BCryptPasswordEncoder().encode(pss1));
                usuarioRepository.save(usuario);
                model.addAttribute("msg", "Contraseña actualizada");
                return "iniciarSesion";
            }else {
                model.addAttribute("msg", "El correo no está registrado");
                return "iniciarSesion";
            }

        }else {
            model.addAttribute("msg", "Las contraseñas no coinciden.");
            return "iniciarSesion";
        }

    }





    @Autowired
    private Email email;

    @PostMapping("/")
    public String sendEmail(@RequestParam("correo") String correo, Model model) {

        String subject="Recuperacion de contraseña";
        String content="Funcionaaaaa!!";
        email.sendMail(correo, subject, content);
        return "redirec:/recuperar";
    }

    @PostMapping("/processLogin")
    public String registrarCuenta(@RequestParam("nombre") String nombre,
                              @RequestParam("apellido") String apellido,
                              @RequestParam("dni") String dni,
                              @RequestParam("correo") String correo,
                              @RequestParam("contraseña") String contrasenha,
                              @RequestParam("cont2") String confirmar,
                              Model model, RedirectAttributes attr,
                              HttpSession session){
        if(!"".equals(contrasenha) && !"".equals(confirmar)){
            if(contrasenha.equals(confirmar)){
                attr.addFlashAttribute("msg", "Contraseña actualizada.");
                Usuario usuarioLog=(Usuario) session.getAttribute("user");
                usuarioLog.setContrasenha(contrasenha);
                session.setAttribute("user", usuarioLog);
                return "redirect:/admin";
            }else{
                attr.addFlashAttribute("msg", "Las contraseñas no coinciden");
                return "redirect:/registrarse";
            }
        }else{
            attr.addFlashAttribute("msg", "No puede haber campos vacíos.");
            return "redirect:/registrarse";
        }
    }

    @PostMapping("/buscador")
    public String buscador(Model model, @RequestParam("search") String search){

        if(search.equalsIgnoreCase("")){
            return "redirect:/list";
        }else{
            String buscar = search + "%";
            model.addAttribute("lista",productoRepository.Buscador(buscar));
            return "index2";
        }


    }

}
