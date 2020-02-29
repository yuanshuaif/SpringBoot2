package springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.entity.User;
import springboot.mybatis.annotationMapper.SimpleMapper;
import springboot.mybatis.mapper.SimpleXmlMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisApplicationTests {

	@Autowired
	private SimpleMapper simpleMapper;// 无xml
	@Autowired
	private SimpleXmlMapper simpleXmlMapper;// 有xml
	@Test
	public void test1() {
		System.out.println(simpleMapper.getAll().toString());
	}
	@Test
	public void test2() {
		User user = new User();
		user.setId(2);
		user.setName("ltj");
		user.setAge("18");
		simpleMapper.insert(user);
	}
	@Test
	public void test3() {
		User user = new User();
		user.setId(1);
		user.setName("dk");
		user.setAge("28");
		simpleMapper.update(user);
	}

	@Test
	public void testMXml1() {
		System.out.println(simpleXmlMapper.getAll().toString());
	}
	@Test
	public void testXml2() {
		User user = new User();
		user.setId(8);
		user.setName("ltj");
		user.setAge("18");
		simpleXmlMapper.insert(user);
	}
	@Test
	public void testXml3() {
		User user = new User();
		user.setId(1);
		user.setName("ltj");
		user.setAge("28");
		simpleXmlMapper.update(user);
	}
}
