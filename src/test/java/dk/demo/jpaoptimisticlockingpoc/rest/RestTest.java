package dk.demo.jpaoptimisticlockingpoc.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.demo.jpaoptimisticlockingpoc.repository.DrumsRepository;
import dk.demo.jpaoptimisticlockingpoc.dto.DrumsDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Drums;
import dk.demo.jpaoptimisticlockingpoc.entity.Guitar;
import dk.demo.jpaoptimisticlockingpoc.repository.GuitarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@AutoConfigureMockMvc
@SpringBootTest
public class RestTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GuitarRepository guitarRepository;

    @Autowired
    private DrumsRepository drumsRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        guitarRepository.deleteAll();
        drumsRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getGuitarTest() throws Exception{
        Guitar guitar = new Guitar();
        guitar.setColor("Blue");
        guitar.setType("Gibson SG");
        guitar.setYear(1969);
        Guitar savedGuitar = guitarRepository.save(guitar);

        MvcResult response = mockMvc.perform(get("/guitars/" + savedGuitar.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Guitar actualGuitar = new ObjectMapper().readValue(json, Guitar.class);
        assertEquals(savedGuitar, actualGuitar);
    }

    @Test
    void getAllDrumsTest() throws Exception{
        Drums drums1 = new Drums();
        drums1.setDecibels(200);
        drums1.setColor("Black");
        drums1.setManufacturer("DW");
        drumsRepository.save(drums1);

        Drums drums2 = new Drums();
        drums2.setDecibels(200);
        drums2.setColor("White");
        drums2.setManufacturer("Mapex");
        drumsRepository.save(drums2);

        MvcResult response = mockMvc.perform(get("/drums")).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        List<Drums> actualAllDrums = new ObjectMapper().readValue(json, new TypeReference<List<Drums>>(){});
        assertEquals(actualAllDrums.size(), 2);
    }

    @Test
    void getDrumsTest() throws Exception{
        Drums drums = new Drums();
        drums.setDecibels(200);
        drums.setColor("Black");
        drums.setManufacturer("DW");
        Drums savedDrums = drumsRepository.save(drums);

        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);
        assertEquals(savedDrums, actualDrums);
    }

    @Test
    void concurrentDrumsUpdateTest() throws Exception{
        Drums drums = new Drums();
        drums.setDecibels(200);
        drums.setColor("Black");
        drums.setManufacturer("DW");
        Drums savedDrums = drumsRepository.save(drums);

        // GET
        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);

        // 1st UDATE
        actualDrums.setDecibels(250);
        mockMvc.perform(put("/drums/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 2st UDATE
        actualDrums.setDecibels(280);
        mockMvc.perform(put("/drums/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Drums> result = drumsRepository.findById(savedDrums.getId());
        assertEquals(result.get().getDecibels(), 280);
    }

    @Test
    void concurrentDrumsUpdateWithDetachTest() throws Exception{
        Drums drums = new Drums();
        drums.setDecibels(200);
        drums.setColor("Black");
        drums.setManufacturer("DW");
        Drums savedDrums = drumsRepository.save(drums);

        // GET
        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);

        // 1st UDATE
        actualDrums.setDecibels(250);
        mockMvc.perform(put("/drums/detach/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThrows(NestedServletException.class, () -> {
            // 2st UDATE
            actualDrums.setDecibels(280);
            mockMvc.perform(put("/drums/detach/" + actualDrums.getId())
                    .content(new ObjectMapper().writeValueAsString(actualDrums))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        });
    }

    @Test
    void concurrentDrumsUpdateAttachedTest() throws Exception{
        Drums drums = new Drums();
        drums.setDecibels(200);
        drums.setColor("Black");
        drums.setManufacturer("DW");
        Drums savedDrums = drumsRepository.save(drums);

        // GET
        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);

        // 1st UDATE
        actualDrums.setDecibels(250);
        mockMvc.perform(put("/drums/attached/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 2st UDATE
        actualDrums.setDecibels(280);
        mockMvc.perform(put("/drums/attached/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateNormalUserBeforeEiti() throws Exception{
        Drums savedDrums = createAndSaveDrums();

        // GET
        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);

        // 1st UDATE
        actualDrums.setDecibels(250);
        mockMvc.perform(put("/drums/detach/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 2st UDATE
        actualDrums.setDecibels(280);
        String result = mockMvc.perform(put("/drums/attached/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final DrumsDto resultDto = (new ObjectMapper()).readValue(result, DrumsDto.class);
        assertEquals(280, resultDto.getDecibels());
        assertEquals(280, resultDto.getDecibels());
    }

    @Test
    void updateEitiBeforeNormalUser() throws Exception{
        Drums savedDrums = createAndSaveDrums();

        // GET
        MvcResult response = mockMvc.perform(get("/drums/" + savedDrums.getId())).andExpect(status().isOk()).andReturn();
        String json = response.getResponse().getContentAsString();
        Drums actualDrums = new ObjectMapper().readValue(json, Drums.class);

        // 1st UDATE
        actualDrums.setDecibels(250);
        mockMvc.perform(put("/drums/attached/" + actualDrums.getId())
                .content(new ObjectMapper().writeValueAsString(actualDrums))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 2st UDATE
        actualDrums.setDecibels(280);
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(put("/drums/detach/" + actualDrums.getId())
                    .content(new ObjectMapper().writeValueAsString(actualDrums))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
                    //.andExpect(content().)
        });
    }

    private Drums createAndSaveDrums() {
        Drums drums = new Drums();
        drums.setDecibels(200);
        drums.setColor("Black");
        drums.setManufacturer("DW");

        return drumsRepository.save(drums);
    }
}
