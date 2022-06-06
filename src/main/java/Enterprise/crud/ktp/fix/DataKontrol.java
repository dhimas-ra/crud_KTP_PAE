/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enterprise.crud.ktp.fix;

import static Enterprise.crud.ktp.fix.Data_.berlakuhingga;
import static Enterprise.crud.ktp.fix.Data_.nama;
import static Enterprise.crud.ktp.fix.Data_.noktp;
import static Enterprise.crud.ktp.fix.Data_.tgllahir;
import Enterprise.crud.ktp.fix.dummy.Dummy;
import Enterprise.crud.ktp.fix.exceptions.NonexistentEntityException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Dhimas
 */
@Controller
public class DataKontrol {

    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tgllahir;

    @GetMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model) {
        int record = datactrl.getDataCount();
        String result = "";
        try {
            newdata = datactrl.findDataEntities().subList(0, record);
        } catch (Exception e) {
            result = e.getMessage();
        }
        model.addAttribute("Data", newdata);
        model.addAttribute("record", record);

        return "database";
    }

    @RequestMapping("/buat")
    public String createDummy(Model m) {
        Data d = new Data();
        m.addAttribute("data", d);
        return "AddData";
    }

    @PostMapping(value = "/databaru", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RedirectView newData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        long id = Integer.parseInt(r.getParameter("id"));
        String noktp = r.getParameter("noktp");
        String nama = r.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        String gender = r.getParameter("gender");
        String alamat = r.getParameter("alamat");
        String agama = r.getParameter("agama");
        String status = r.getParameter("status");
        String pekerjaan = r.getParameter("pekerjaan");
        String asal = r.getParameter("warganegara");
        String berlaku = r.getParameter("berlakuhingga");
        byte[] gmbr = f.getBytes();

        d.setId(id);
        d.setNoktp(noktp);
        d.setNama(nama);
        d.setTgllahir(date);
        d.setGender(gender);
        d.setAlamat(alamat);
        d.setAgama(agama);
        d.setStatus(status);
        d.setPekerjaan(pekerjaan);
        d.setWarganegara(agama);
        d.setBerlakuhingga(berlaku);
        d.setFoto(gmbr);

        datactrl.create(d);
        return new RedirectView("/data");
    }

    @RequestMapping(value = "/gmbr", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") long id) throws Exception {
        Data d = datactrl.findData(id);
        byte[] gmbr = d.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(gmbr);
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable long id, Model m) throws NonexistentEntityException {
        Data d = new Data();
        
        try{
            d = datactrl.findData(id);
        }catch(Exception e){
            
        }
        
        String foto = "";
        if(d != null){
            foto = Base64.encodeBase64String(d.getFoto());
            m.addAttribute("foto", foto);
        }
        
        m.addAttribute("data", d);
        
        return "detail";
    }

    @RequestMapping("/main")
    public String getMain() {
        return "menu";
    }

    @GetMapping("/hapus/{id}")

    public String delete(@PathVariable(value = "id") long id) throws NonexistentEntityException {

        datactrl.destroy(id);
        return "redirect:/data";
    }

    @RequestMapping("/ubah/{id}")
    public String update(@PathVariable(value = "id") long id, Model m) throws NonexistentEntityException {
        Data d = datactrl.findData(id);
        m.addAttribute("data", d);
        return "editktp";
    }

    @PostMapping(value = "/perbarui", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView updatData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();
        
        long id = Integer.parseInt(r.getParameter("id"));
        String noktp = r.getParameter("noktp");
        String nama = r.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        String gender = r.getParameter("gender");
        String alamat = r.getParameter("alamat");
        String agama = r.getParameter("agama");
        String status = r.getParameter("status");
        String pekerjaan = r.getParameter("pekerjaan");
        String asal = r.getParameter("warganegara");
        String berlaku = "Seumur Hidup";
        byte[] gmbr = f.getBytes();

        d.setId(id);
        d.setNoktp(noktp);
        d.setNama(nama);
        d.setTgllahir(date);
        d.setGender(gender);
        d.setAlamat(alamat);
        d.setAgama(agama);
        d.setStatus(status);
        d.setPekerjaan(pekerjaan);
        d.setWarganegara(agama);
        d.setBerlakuhingga(berlaku);
        d.setFoto(gmbr);

        datactrl.edit(d);
        return new RedirectView("/data");
    }
}
