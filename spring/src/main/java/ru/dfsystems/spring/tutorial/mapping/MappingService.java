package ru.dfsystems.spring.tutorial.mapping;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.dfsystems.spring.tutorial.dao.course.CourseDaoImpl;
import ru.dfsystems.spring.tutorial.dao.instrument.InstrumentDaoImpl;
import ru.dfsystems.spring.tutorial.dao.lesson.LessonDaoImpl;
import ru.dfsystems.spring.tutorial.dao.room.RoomDaoImpl;
import ru.dfsystems.spring.tutorial.dao.student.StudentDaoImpl;
import ru.dfsystems.spring.tutorial.dao.teacher.TeacherDaoImpl;
import ru.dfsystems.spring.tutorial.dto.course.CourseDto;
import ru.dfsystems.spring.tutorial.dto.course.CourseHistoryDto;
import ru.dfsystems.spring.tutorial.dto.course.CourseListDto;
import ru.dfsystems.spring.tutorial.dto.instrument.InstrumentDto;
import ru.dfsystems.spring.tutorial.dto.instrument.InstrumentHistoryDto;
import ru.dfsystems.spring.tutorial.dto.instrument.InstrumentListDto;
import ru.dfsystems.spring.tutorial.dto.lesson.LessonDto;
import ru.dfsystems.spring.tutorial.dto.lesson.LessonHistoryDto;
import ru.dfsystems.spring.tutorial.dto.lesson.LessonListDto;
import ru.dfsystems.spring.tutorial.dto.room.RoomDto;
import ru.dfsystems.spring.tutorial.dto.room.RoomHistoryDto;
import ru.dfsystems.spring.tutorial.dto.student.StudentDto;
import ru.dfsystems.spring.tutorial.dto.student.StudentHistoryDto;
import ru.dfsystems.spring.tutorial.dto.student.StudentListDto;
import ru.dfsystems.spring.tutorial.dto.teacher.TeacherDto;
import ru.dfsystems.spring.tutorial.dto.teacher.TeacherHistoryDto;
import ru.dfsystems.spring.tutorial.generated.tables.pojos.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MappingService implements BaseMapping {
    private final ModelMapper modelMapper;
    private final RoomDaoImpl roomDao;
    private final InstrumentDaoImpl instrumentDao;
    private final CourseDaoImpl courseDao;
    private final TeacherDaoImpl teacherDao;
    private final StudentDaoImpl studentDao;
    private final LessonDaoImpl lessonDao;

    @PostConstruct
    public void init() {
        //Дополнительные настройки.
        Converter<Integer, List<RoomHistoryDto>> roomHistory =
                context -> mapList(roomDao.getHistory(context.getSource()), RoomHistoryDto.class);
        Converter<Integer, List<InstrumentListDto>> instrumentList =
                context -> mapList(instrumentDao.getInstrumentsByRoomIdd(context.getSource()), InstrumentListDto.class);

        Converter<Integer, List<CourseHistoryDto>> courseHistory =
                context -> mapList(courseDao.getHistory(context.getSource()), CourseHistoryDto.class);
        Converter<Integer, TeacherDto> courseTeacher =
                mappingContext -> map(teacherDao.getActiveByIdd(mappingContext.getSource()), TeacherDto.class);
        Converter<Integer, List<StudentListDto>> courseStudents =
                mappingContext -> mapList(studentDao.getStudentsByCourseIdd(mappingContext.getSource()), StudentListDto.class);
        Converter<Integer, List<LessonListDto>> courseLessons =
                mappingContext -> mapList(lessonDao.getLessonsByCourseIdd(mappingContext.getSource()), LessonListDto.class);


        Converter<Integer, RoomDto> lessonRoom =
                mappingContext -> map(roomDao.getActiveByIdd(mappingContext.getSource()), RoomDto.class);
        Converter<Integer, CourseDto> lessonCourse =
                mappingContext -> map(courseDao.getActiveByIdd(mappingContext.getSource()), CourseDto.class);
        Converter<Integer, InstrumentListDto> lessonInstruments =
                mappingContext -> map(instrumentDao.getInstrumentsByLessonIdd(mappingContext.getSource()), InstrumentListDto.class);
        Converter<Integer, List<LessonHistoryDto>> lessonHistory =
                context -> mapList(lessonDao.getHistory(context.getSource()), LessonHistoryDto.class);

        Converter<Integer, List<InstrumentHistoryDto>> instrumentHistory =
                context -> mapList(instrumentDao.getHistory(context.getSource()), InstrumentHistoryDto.class);

        Converter<Integer, List<StudentHistoryDto>> studentHistory =
                context -> mapList(studentDao.getHistory(context.getSource()), StudentHistoryDto.class);
        Converter<Integer, List<CourseListDto>> studentCourses =
                context -> mapList(courseDao.getCoursesByStudentIdd(context.getSource()), CourseListDto.class);

        Converter<Integer, List<TeacherHistoryDto>> teacherHistory =
                context -> mapList(teacherDao.getHistory(context.getSource()), TeacherHistoryDto.class);
        Converter<Integer, List<CourseListDto>> teacherCourses =
                context -> mapList(courseDao.getCoursesByTeacherIdd(context.getSource()), CourseListDto.class);


        modelMapper.typeMap(Room.class, RoomDto.class)
                .addMappings(mapper -> mapper.using(roomHistory).map(Room::getIdd, RoomDto::setHistory))
                .addMappings(mapper -> mapper.using(instrumentList).map(Room::getIdd, RoomDto::setInstruments));
        modelMapper.typeMap(Course.class, CourseDto.class)
                .addMappings(mapping -> mapping.using(courseHistory).map(Course::getIdd, CourseDto::setHistory))
                .addMappings(mapping -> mapping.using(courseTeacher).map(Course::getTeacherIdd, CourseDto::setTeacher))
                .addMappings(mapping -> mapping.using(courseStudents).map(Course::getIdd, CourseDto::setStudents))
                .addMappings(mapping -> mapping.using(courseLessons).map(Course::getIdd, CourseDto::setLessons));
        modelMapper.typeMap(Course.class, CourseHistoryDto.class)
                .addMappings(mapping -> mapping.using(courseTeacher).map(Course::getIdd, CourseHistoryDto::setTeacher));
        modelMapper.typeMap(Lesson.class, LessonDto.class)
                .addMappings(mapping -> mapping.using(lessonCourse).map(Lesson::getCourseIdd, LessonDto::setCourse))
                .addMappings(mapping -> mapping.using(lessonRoom).map(Lesson::getRoomIdd, LessonDto::setRoom))
                .addMappings(mapping -> mapping.using(lessonInstruments).map(Lesson::getIdd, LessonDto::setInstruments))
                .addMappings(mapping -> mapping.using(lessonHistory).map(Lesson::getIdd, LessonDto::setHistory));
        modelMapper.typeMap(Lesson.class, LessonListDto.class)
                .addMappings(mapping -> mapping.using(lessonRoom).map(Lesson::getIdd, LessonListDto::setRoom));
        modelMapper.typeMap(Lesson.class, LessonHistoryDto.class)
                .addMappings(mapping -> mapping.using(lessonRoom).map(Lesson::getRoomIdd, LessonHistoryDto::setRoom));
        modelMapper.typeMap(Instrument.class, InstrumentDto.class)
                .addMappings(mapping -> mapping.using(instrumentHistory).map(Instrument::getIdd, InstrumentDto::setHistory));
        modelMapper.typeMap(Student.class, StudentDto.class)
                .addMappings(mapping -> mapping.using(studentHistory).map(Student::getIdd, StudentDto::setHistory))
                .addMappings(mapping -> mapping.using(studentCourses).map(Student::getIdd, StudentDto::setCourses));
        modelMapper.typeMap(Teacher.class, TeacherDto.class)
                .addMappings(mapping -> mapping.using(teacherHistory).map(Teacher::getIdd, TeacherDto::setHistory))
                .addMappings(mapping -> mapping.using(teacherCourses).map(Teacher::getIdd, TeacherDto::setCourses));
    }

    public <S, D> D map(S source, Class<D> clazz) {
        return modelMapper.map(source, clazz);
    }

    public <S, D> void map(S source, D dest) {
        modelMapper.map(source, dest);
    }

    public <S, D> List<D> mapList(List<S> sources, Class<D> clazz){
        return sources.stream()
                .map(s -> map(s, clazz))
                .collect(Collectors.toList());
    }
}
