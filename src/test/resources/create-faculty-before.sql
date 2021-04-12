delete from faculty_subject;
delete from faculty;

insert into faculty(id, budget_places, contract_places, finalized, name) VALUES
(1, 1, 1, true, 'Faculty01'),
(2, 2, 3, false, 'Faculty02'),
(3, 1, 11, false, 'Faculty03');

insert into faculty_subject(faculty_id, exam_subjects) VALUES
    (1,'PHYSICS'),
    (1,'MATHEMATICS'),
    (1,'UKRAINIAN'),
    (2,'GEOGRAPHY'),
    (2,'PHYSICS'),
    (2,'BIOLOGY'),
    (3,'UKRAINIAN'),
    (3,'BIOLOGY'),
    (3,'MATHEMATICS');



