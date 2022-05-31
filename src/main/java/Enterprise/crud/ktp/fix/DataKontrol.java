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

/**
 *
 * @author Dhimas
 */


@Controller
public class DataKontrol {
    
    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();
    
    @DateTimeFormat(pattern="yyyy-MM-dd") 
    private Date tgllahir;

    @GetMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model) {
        int record = datactrl.getDataCount();
        String result = "";
        try {
            newdata = datactrl.findDataEntities().subList(0, record);
        } catch (Exception e) {result=e.getMessage();}
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
   
    
    @PostMapping(value = "/databaru", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String newData(@ModelAttribute("data") Data d, @RequestParam("gambar") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        
        
        byte[] img = f.getBytes();
        
        
        datactrl.create(d);
        return "redirect:/data";
    }
    
     @RequestMapping(value = "/gmbr", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") long id) throws Exception {
        Data d = datactrl.findData(id);
        byte[] img = d.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }
    
    @RequestMapping("/detail")
    public String detail(@PathVariable(value = "id") long id, Model m) throws NonexistentEntityException {
        Data d = datactrl.findData(id);
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
    @ResponseBody
    public String updateDummyData(@RequestParam("gambar") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tanggal"));
        byte[] img = f.getBytes();
        

        datactrl.edit(d);
        return "updated";
    }
}
