package rs.ac.uns.ftn.informatika.jpa.controller.primer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.informatika.jpa.dto.primer.ExamDTO;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Course;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Exam;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.service.primer.CourseService;
import rs.ac.uns.ftn.informatika.jpa.service.primer.ExamService;
import rs.ac.uns.ftn.informatika.jpa.service.primer.StudentService;

@RestController
@RequestMapping(value = "api/exams")
public class ExamController {

	@Autowired
	private ExamService examService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;

	@PostMapping(consumes = "application/json")
	public ResponseEntity<ExamDTO> createExam(@RequestBody ExamDTO examDTO) {

		// a new exam must have student and course defined
		if (examDTO.getStudent() == null || examDTO.getCourse() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Student student = studentService.findOneWithExams(examDTO.getStudent().getId());
		Course course = courseService.findOneWithExams(examDTO.getCourse().getId());

		if (student == null || course == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Exam exam = new Exam();
		exam.setDate(examDTO.getDate());
		exam.setGrade(examDTO.getGrade());
		exam.setStudent(student);
		exam.setCourse(course);
		course.addExam(exam);
		student.addExam(exam);

		exam = examService.save(exam);
		return new ResponseEntity<>(new ExamDTO(exam), HttpStatus.CREATED);
	}

	@PutMapping(consumes = "application/json")
	public ResponseEntity<ExamDTO> updateExam(@RequestBody ExamDTO examDTO) {

		// an exam must exist
		Exam exam = examService.findOne(examDTO.getId());
		if (exam == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		// we allow changing date and points for an exam only
		exam.setDate(examDTO.getDate());
		exam.setGrade(examDTO.getGrade());

		exam = examService.save(exam);
		return new ResponseEntity<>(new ExamDTO(exam), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteExam(@PathVariable Integer id) {

		Exam exam = examService.findOne(id);

		if (exam != null) {
			examService.remove(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
