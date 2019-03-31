package {{packageName}};

{{testImports}}
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import {{packageName}}.{{applicationName}};

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {{applicationName}}.class)
@AutoConfigureMockMvc
@WebAppConfiguration
{{testAnnotations}}public class {{applicationName}}Tests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/hello").contentType(MediaType.APPLICATION_JSON)).
        andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(print())//输出MvcResult到控制台
        .andReturn();
        int status = mockResult.getResponse().getStatus();
        System.err.println(mockResult.getResponse().getContentAsString());
    }

    @Test
    public void list() throws Exception {
        MvcResult mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/list").contentType(MediaType.APPLICATION_JSON)).
        andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.[*].a").value("a"))//使用Json path验证JSON 请参考http://goessner.net/articles/JsonPath/
        .andDo(print())//输出MvcResult到控制台
        .andReturn();
        int status = mockResult.getResponse().getStatus();
        System.err.println(mockResult.getResponse().getContentAsString());
    }

}

