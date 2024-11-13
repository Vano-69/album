package telran.album.dao;

import telran.album.model.Photo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlbumImpl implements Album {
    private Photo[] photos;
    private int size;

    public AlbumImpl(int capacity) {
        photos = new Photo[capacity];
    }

    @Override
    public boolean addPhoto(Photo photo) {
        if (photo == null || photos.length == size
                || getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbumId()) != null) {
            return false;
        }
        photos[size++] = photo;
        return true;
    }

    @Override
    public boolean removePhoto(int photoId, int albumId) {
        Photo pattern = new Photo(albumId, photoId, null, null, null);
        for (int i = 0; i < size; i++) {
            if (pattern.equals(photos[i])) {
                System.arraycopy(photos, i + 1, photos, i, size - i - 1);
                photos[--size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePhoto(int photoId, int albumId, String url) {
        Photo photo = getPhotoFromAlbum(photoId, albumId);
        if (photo == null) {
            return false;
        }
        photo.setUrl(url);
        return true;
    }

    @Override
    public Photo getPhotoFromAlbum(int photoId, int albumId) {
        for (int i = 0; i < size; i++) {
            if (photos[i].getAlbumId() == albumId && photos[i].getPhotoId() == photoId) {
                return photos[i];
            }
        }
        return null;
    }

    @Override
    public Photo[] getAllPhotoFromAlbum(int albumId) {
        return findPicturesByPredicate(p -> p.getAlbumId() == albumId);
    }

    @Override
    public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
        return findPicturesByPredicate(p -> p.getDate().compareTo(dateFrom.atStartOfDay())>=0
        && p.getDate().compareTo(dateTo.atTime(LocalTime.MAX)) <=0);
    }

    @Override
    public int size() {
        return size;
    }

    //==========_________findPicturesByPredicate____===========
    private Photo[] findPicturesByPredicate(Predicate<Photo> predicate) {
        Photo[] arrayTemporary = new Photo[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(photos[i])) {
                arrayTemporary[j++] = photos[i];
            }
        }
        return Arrays.copyOf(arrayTemporary, j);
    }
}


//==============____метод addPhoto_____===================================
/*
Этот метод addPhoto добавляет фотографию в массив фотографий, если выполняются определенные
 условия. Вот поэтапное описание работы этого кода:

Шаг 1: Проверка условий для добавления фотографии
Метод проверяет несколько условий, при которых добавление фотографии невозможно:

Проверка на null:

Если фотография равна null, она не будет добавлена.
java
Копировать код
if (photo == null || ...
Проверка, есть ли место в массиве:

Проверяется, заполнен ли массив фотографий. Если текущее количество фотографий (size)
 равно длине массива (photos.length), массив переполнен, и добавить новую фотографию невозможно.
java
Копировать код
|| photos.length == size
Проверка на дубликат:

Метод проверяет, есть ли уже фотография с таким же идентификатором альбома и фотографии.
Он вызывает метод getPhotoFromAlbum, который должен возвращать фотографию по её идентификатору
и идентификатору альбома. Если фотография уже существует, её не добавляют.
java
Копировать код
|| getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbumId()) != null
Шаг 2: Добавление фотографии
Если ни одно из вышеперечисленных условий не выполнено (то есть фотография допустима для
добавления), происходит её добавление:

Добавление фотографии:
Фотография добавляется в массив photos на позицию, указанную переменной size. Переменная
size указывает текущее количество фотографий в альбоме.
java
Копировать код
photos[size++] = photo;
После добавления фотография размещается на позицию size, и переменная size увеличивается
на 1, чтобы указать на следующую свободную ячейку.
Шаг 3: Возвращение результата
Возвращение результата:
Если фотография была успешно добавлена, метод возвращает true. Если хотя бы одно из
проверочных условий оказалось истинным (фотография null, массив полон или фотография
уже существует), метод возвращает false.
Пример работы:
Пример 1: Успешное добавление

Допустим, у нас есть массив фотографий длиной 5, и текущий размер size равен 3. Если
мы передаем валидную фотографию, которая не является null, массив не заполнен, и такой
фотографии ещё нет, она будет добавлена в массив на позицию 3, и size станет равным 4.
Метод вернет true.
Пример 2: Ошибка добавления (массив переполнен):

Если массив уже заполнен (например, size == photos.length), фотография не будет добавлена,
и метод вернёт false.
Пример 3: Ошибка добавления (дубликат):

Если в массиве уже существует фотография с таким же photoId и albumId, метод не добавит её
и вернёт false.
Важные моменты:
Метод getPhotoFromAlbum: Этот метод должен быть реализован так, чтобы находить фотографию
по её идентификатору и идентификатору альбома. Если он работает неправильно, метод addPhoto
может либо ошибочно не добавлять фотографию, либо создавать дубликаты.

Размер массива: Массив фиксированной длины (photos.length), поэтому добавление фотографии
возможно только до тех пор, пока есть свободное место. Если требуется динамическое увеличение
массива (как в коллекциях, например ArrayList), этот код нужно было бы доработать.

Увеличение переменной size: Увеличение размера происходит сразу после добавления фотографии с
помощью постфиксного инкремента size++. Это гарантирует, что следующая фотография будет
добавлена на следующую свободную позицию.
 */

// ============____метод removePhoto____!!!!!!!!!!!!!!!!!!!! ask question_=====
/*
Этот метод removePhoto удаляет фотографию с указанным идентификатором photoId из альбома с
идентификатором albumId. Вот как работает этот код:

Основные шаги:
Создание паттерна для поиска:
Метод сначала создает объект Photo patttern с указанными albumId и photoId, а остальные
параметры (title, url, date) остаются null. Этот объект будет использоваться для поиска
фотографии, соответствующей по идентификаторам альбома и фотографии.
java
Копировать код
Photo patttern = new Photo(albumId, photoId, null, null, null);
Поиск фотографии:

Метод проходит через массив photos[] с текущим количеством фотографий size в альбоме.
Для каждой фотографии проверяется, совпадает ли она с паттерном. Это происходит с помощью
метода equals, который должен быть реализован в классе Photo.
java
Копировать код
for (int i = 0; i < size; i++) {
    if (patttern.equals(photos[i])) {
Если patttern.equals(photos[i]) возвращает true, то текущая фотография считается найденной.
Удаление фотографии:

Если фотография найдена, метод удаляет её, используя System.arraycopy для сдвига всех
последующих элементов на одну позицию влево, начиная с позиции найденной фотографии.
java
Копировать код
System.arraycopy(photos, i + 1, photos, i, size - i - 1);
Это позволяет избежать "дыр" в массиве после удаления элемента.

После сдвига, последний элемент массива (который оказался дублированным из-за сдвига)
обнуляется.

java
Копировать код
photos[--size] = null;
Размер массива size уменьшается на 1, так как количество фотографий теперь уменьшилось.
Возвращение результата:

Если фотография была найдена и удалена, метод возвращает true, что означает успешное удаление.
java
Копировать код
return true;
Фотография не найдена:

Если цикл завершился, и фотография с указанными идентификаторами не была найдена, метод возвращает
false.
java
Копировать код
return false;
Как работает метод на практике:
Пример вызова: Если вы вызовете removePhoto(1, 2), метод создаст объект Photo с albumId=1 и
photoId=2 и будет искать фотографию с такими же идентификаторами в массиве photos. Если найдёт
такую фотографию, она будет удалена, и все последующие фотографии будут сдвинуты на одну позицию влево.

Использование System.arraycopy: Это метод стандартной библиотеки Java, который копирует часть массива.
В данном случае он используется для сдвига элементов массива, начиная с позиции найденной фотографии,
чтобы удалить её.

Важные моменты:
Метод equals: Этот метод зависит от реализации метода equals в классе Photo. Он должен сравнивать
фотографии по их albumId и photoId. Если метод equals не реализован корректно, удаление может
не сработать.
!!!!!!!!!!!!!!!!!!!!:
Работа с массивами: Это не самый оптимальный способ удаления элементов, поскольку каждый раз,
когда вы удаляете элемент, все последующие элементы должны быть сдвинуты. Это может быть
неэффективно при большом количестве фотографий.

Пример работы с массивом:
Предположим, у нас есть массив фотографий:

csharp
Копировать код
[Photo1, Photo2, Photo3, Photo4]
Если мы удаляем Photo2, массив после удаления будет выглядеть так:

csharp
Копировать код
[Photo1, Photo3, Photo4, null]
Значение null указывает на свободное место в массиве после удаления.
 */

//===============_________метод updatePhoto_________===========
/*
Этот метод updatePhoto обновляет URL фотографии в альбоме по её идентификатору photoId
 и идентификатору альбома albumId. Вот как работает этот код шаг за шагом:

Шаг 1: Поиск фотографии в альбоме
Получение фотографии:

Метод вызывает getPhotoFromAlbum(photoId, albumId) для того, чтобы найти фотографию с
указанными идентификаторами. Этот метод должен вернуть объект Photo, если фотография
существует в альбоме, или null, если такой фотографии нет.
java
Копировать код
Photo photo = getPhotoFromAlbum(photoId, albumId);
Проверка на наличие фотографии:

Если метод getPhotoFromAlbum вернул null, это означает, что фотография с такими
идентификаторами не найдена в альбоме. В этом случае метод возвращает false, сигнализируя,
 что обновление не удалось.
java
Копировать код
if (photo == null) {
    return false;
}
Шаг 2: Обновление URL
Обновление URL фотографии:
Если фотография найдена (то есть photo не равен null), метод вызывает photo.setUrl(url)
для обновления URL фотографии на новое значение, переданное в аргументе метода.
java
Копировать код
photo.setUrl(url);
Шаг 3: Возвращение результата
Возвращение результата:
После того как URL фотографии был обновлён, метод возвращает true, указывая, что операция
прошла успешно.
java
Копировать код
return true;
Пример работы:
Пример 1: Успешное обновление:

Допустим, в альбоме есть фотография с photoId = 1 и albumId = 1. Если мы вызовем
updatePhoto(1, 1, "newUrl"), то метод найдёт эту фотографию с помощью getPhotoFromAlbum,
обновит её URL на "newUrl" и вернёт true.
Пример 2: Неудачное обновление (фотография не найдена):

Если мы вызовем updatePhoto(2, 1, "newUrl"), но фотографии с photoId = 2 в альбоме с
albumId = 1 не существует, метод вернёт false, поскольку getPhotoFromAlbum вернёт null.
Важные моменты:
Метод getPhotoFromAlbum:

Этот метод должен корректно находить фотографию по её идентификаторам. Если он работает
неправильно (например, ищет по неправильным критериям), метод updatePhoto может вернуть
ложное значение или обновить неправильную фотографию.
Метод setUrl:

Для корректной работы важно, чтобы в классе Photo был реализован метод setUrl(String url),
который обновляет поле url объекта фотографии.
Проверка наличия:

Если фотография не найдена, метод сразу возвращает false, что позволяет быстро завершить
выполнение, если обновление невозможно.
Этот код обновляет URL фотографии только при условии, что фотография найдена, и предотвращает
обновление, если фотография не существует в альбоме.
 */

//==============_______Метод getAllPhotoFromAlbum + Predicate_______==============
/*
Этот код реализует метод для получения всех фотографий из конкретного альбома по его
идентификатору. Давайте разберём, как это работает:

Метод getAllPhotoFromAlbum
Параметр метода:

Метод принимает один параметр albumId — это идентификатор альбома, из которого нужно
получить все фотографии.
java
Копировать код
public Photo[] getAllPhotoFromAlbum(int albumId) {
Вызов метода findPicturesByPredicate:

Метод использует функциональный интерфейс Predicate<Photo>, чтобы задать условие для
поиска фотографий. В данном случае это условие заключается в том, что идентификатор
альбома фотографии (p.getAlbumId()) должен совпадать с переданным albumId.
Выражение p -> p.getAlbumId() == albumId является лямбда-выражением, которое проверяет
это условие для каждой фотографии.
java
Копировать код
return findPicturesByPredicate(p -> p.getAlbumId() == albumId);
Результат:

Метод возвращает результат выполнения findPicturesByPredicate, то есть массив фотографий,
идентификатор альбома которых совпадает с albumId.
Метод findPicturesByPredicate
Создание временного массива:

Этот метод принимает предикат Predicate<Photo>, который содержит условие для фильтрации
фотографий.
В начале создаётся временный массив arrayTemporary длиной size, который будет хранить
подходящие фотографии.
java
Копировать код
Photo[] arrayTemporary = new Photo[size];
Поиск фотографий с использованием предиката:

Метод проходит через все фотографии в массиве photos[] с помощью цикла for и применяет
предикат к каждой фотографии.
Если фотография удовлетворяет условию предиката (возвращает true), она добавляется во
временный массив на позицию j, и j увеличивается на 1.
java
Копировать код
for (int i = 0; i < size; i++) {
    if (predicate.test(photos[i])) {
        arrayTemporary[j++] = photos[i];
    }
}
Обрезка массива до фактического размера:

После завершения цикла, чтобы убрать пустые элементы в конце временного массива, метод
возвращает копию массива до индекса j. Это копирование удаляет все неиспользуемые ячейки массива.
java
Копировать код
return Arrays.copyOf(arrayTemporary, j);
Как работают вместе getAllPhotoFromAlbum и findPicturesByPredicate
Лямбда-выражение в getAllPhotoFromAlbum:

Метод getAllPhotoFromAlbum передает в findPicturesByPredicate лямбда-выражение
p -> p.getAlbumId() == albumId. Это выражение используется как предикат, который проверяет
каждую фотографию в массиве photos[] и фильтрует те, у которых идентификатор альбома равен albumId.
Процесс фильтрации:

Метод findPicturesByPredicate проходит по всему массиву photos[] и добавляет в новый массив только
 те фотографии, которые удовлетворяют предикату (в данном случае, у которых albumId совпадает с
 переданным значением).
Результат:

Результатом работы является массив всех фотографий, принадлежащих конкретному альбому с указанным
albumId. Если таких фотографий нет, возвращается пустой массив.
Пример работы:
Допустим, у нас есть фотографии в массиве photos[], и мы вызываем:

java
Копировать код
Photo[] photosFromAlbum = getAllPhotoFromAlbum(1);
Это вызовет метод findPicturesByPredicate, который пройдёт по каждой фотографии и отберёт те,
у которых getAlbumId() == 1. Результатом будет массив всех фотографий, принадлежащих альбому с
идентификатором 1.

Важные моменты:
Предикат:

Предикат — это универсальный способ задать условие для фильтрации. В данном случае он проверяет
только albumId, но с тем же успехом можно было бы искать фотографии по другим критериям
(например, по дате или названию).
Обрезка массива:

Чтобы избежать лишних пустых ячеек в возвращаемом массиве, метод копирует временный массив
до индекса j, который содержит количество найденных фотографий.
Безопасность кода:

Если в альбоме нет фотографий с указанным albumId, метод вернёт пустой массив, а не null,
что является хорошей практикой для предотвращения ошибок при работе с пустыми данными.
========================================================
 */
//==========_____________метод findPicturesByPredicate_________====================
/*
Этот метод findPicturesByPredicate ищет фотографии в массиве photos[], которые удовлетворяют
 условию, заданному в виде предиката (Predicate<Photo>). Давайте разберем, как работает этот код:

Шаг 1: Создание временного массива
Создание временного массива:

В начале создаётся массив arrayTemporary длиной size, равный текущему количеству фотографий
в массиве photos. Этот массив используется для временного хранения фотографий, которые
удовлетворяют предикату.
java
Копировать код
Photo[] arrayTemporary = new Photo[size];
Переменная size — это текущее количество фотографий в альбоме.
Переменная-счётчик:

Переменная j инициализируется нулём и будет отслеживать количество найденных фотографий,
которые удовлетворяют предикату, а также использоваться как индекс для вставки фотографий
в массив arrayTemporary.
java
Копировать код
int j = 0;
Шаг 2: Проход по массиву фотографий
Проход по массиву photos[]:

Цикл проходит через все элементы массива photos[] от 0 до size (текущее количество фотографий).
java
Копировать код
for (int i = 0; i < size; i++) {
Проверка каждого элемента с помощью предиката:

Для каждой фотографии вызывается метод predicate.test(photos[i]). Предикат — это функциональный
 интерфейс, который проверяет, удовлетворяет ли объект определённому условию
  (возвращает true или false). В данном случае это фото.
java
Копировать код
if (predicate.test(photos[i])) {
Добавление подходящих фотографий в временный массив:

Если фотография удовлетворяет условию предиката (возвращает true), она добавляется в массив
arrayTemporary на позицию j, после чего j увеличивается на 1.
java
Копировать код
arrayTemporary[j++] = photos[i];
Шаг 3: Возвращение результата
Обрезка массива до фактического размера:

Поскольку временный массив arrayTemporary был инициализирован размером с исходный массив photos[],
в нём может оказаться больше пустых элементов, чем фактически подходящих фотографий.

Метод Arrays.copyOf(arrayTemporary, j) возвращает новый массив, который содержит только те
элементы, что были добавлены (до индекса j). Это позволяет отбросить "лишние" ячейки в конце массива.

java
Копировать код
return Arrays.copyOf(arrayTemporary, j);
Таким образом, если было найдено, например, 3 подходящих фотографии, возвращаемый массив
будет длиной 3 и будет содержать только эти фотографии.
Пример работы:
Предположим, у нас есть массив фотографий и предикат, который проверяет, сделана ли фотография
в определённой дате. Пример предиката может выглядеть так:

java
Копировать код
Predicate<Photo> isFromToday = photo -> photo.getDate().equals(LocalDate.now());
Если этот предикат передать в метод findPicturesByPredicate, он вернёт массив фотографий,
сделанных сегодня.

Важные моменты:
Предикат:

Предикат (Predicate<Photo>) — это функциональный интерфейс, который принимает объект типа
Photo и возвращает true или false, проверяя, соответствует ли фотография какому-либо условию.
 Например, предикат может фильтровать фотографии по дате, названию или другим атрибутам.
Обрезка массива:

Массив обрезается до размера j с помощью метода Arrays.copyOf. Это важно, потому что массив
arrayTemporary может содержать больше элементов, чем нужно (те ячейки, которые остались пустыми,
 обрезаются).
Эффективность:

Временный массив используется для того, чтобы не изменять исходный массив photos[] во время
 фильтрации, и это делается, чтобы избежать проблем с изменением размеров массива или потерей данных.
Однако использование временного массива длиной size может оказаться избыточным, если мы ожидаем,
что найденных фотографий будет гораздо меньше.
Итог:
Метод findPicturesByPredicate возвращает массив фотографий, которые удовлетворяют условиям
предиката, без изменения исходного массива.
___________________________GPT Edge_______________
Объяснение работы метода:
Аргумент предиката:

Метод принимает предикат Predicate<Photo> predicate, который представляет условие для проверки
фотографий.
Предикат — это функциональный интерфейс, содержащий метод test, который возвращает true, если
объект Photo соответствует условию, и false, если не соответствует.
Создание результирующего массива:

Создается временный массив res размером size — текущего количества фотографий в массиве photos.
Этот массив будет использоваться для хранения найденных фотографий, соответствующих условию предиката.
Поиск фотографий с помощью цикла:

Переменная j используется для отслеживания количества найденных фотографий, которые удовлетворяют
 условию.
В цикле for метод перебирает массив photos и для каждой фотографии вызывает predicate.test(photos[i]):
Если предикат возвращает true, значит, текущая фотография photos[i] соответствует условию, и она
добавляется в массив res на позицию j, после чего j увеличивается на единицу.
Если предикат возвращает false, фотография не добавляется в res.
Копирование и возвращение результатов:

После завершения цикла for, временный массив res может содержать пустые элементы (если некоторые
фотографии не соответствовали предикату).
Метод возвращает массив с найденными фотографиями, обрезая массив res до длины j с помощью
Arrays.copyOf(res, j), чтобы исключить пустые элементы.
Пример использования:
Предположим, что у вас есть массив фотографий, и вы хотите найти все фотографии, которые были
добавлены до определенной даты:

java
Copy code
Photo[] recentPhotos = findPicturesByPredicate(photo -> photo.getDate().isBefore(LocalDateTime.now().minusDays(7)));

Здесь предикат photo -> photo.getDate().isBefore(LocalDateTime.now().minusDays(7)) проверяет,
сделана ли фотография более чем 7 дней назад.

Итог:
Метод findPicturesByPredicate — это универсальный способ фильтрации фотографий по любому условию.
Предикат позволяет гибко задавать фильтрацию, и метод возвращает только те фотографии, которые
этому условию соответствуют.
 */
//=============________етод getPhotoBetweenDate_______________===================
/*
Этот код реализует метод getPhotoBetweenDate, который возвращает массив фотографий, сделанных между двумя указанными датами (dateFrom и dateTo). Для поиска используется предикат — логическое условие, которое фильтрует фотографии по дате.

Метод getPhotoBetweenDate
Параметры метода:

Метод принимает два параметра типа LocalDate: dateFrom и dateTo. Эти даты указывают диапазон, в котором должны находиться фотографии.
java
Копировать код
public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
Вызов метода findPicturesByPredicate с предикатом:

Метод использует лямбда-выражение как предикат для фильтрации фотографий по дате:
java
Копировать код
p -> p.getDate().compareTo(dateFrom.atStartOfDay()) > 0 && p.getDate().compareTo(dateTo.atTime(LocalTime.MAX)) < 0
Что проверяет это лямбда-выражение?:

p.getDate() возвращает дату фотографии.
dateFrom.atStartOfDay() преобразует дату dateFrom в LocalDateTime, соответствующее началу дня (00:00:00).
dateTo.atTime(LocalTime.MAX) преобразует дату dateTo в LocalDateTime, соответствующее самому концу дня (23:59:59).
Логическое выражение проверяет:

Дата фотографии (p.getDate()) должна быть позже начала дня dateFrom (compareTo(dateFrom.atStartOfDay()) > 0).
Дата фотографии должна быть раньше конца дня dateTo (compareTo(dateTo.atTime(LocalTime.MAX)) < 0).
Это означает, что метод ищет фотографии, сделанные в промежутке между указанными датами (без включения граничных дат).

Возвращение результата:

Метод возвращает результат работы метода findPicturesByPredicate, который содержит фотографии, удовлетворяющие этому предикату:
java
Копировать код
return findPicturesByPredicate(p -> p.getDate().compareTo(dateFrom.atStartOfDay())>0
     && p.getDate().compareTo(dateTo.atTime(LocalTime.MAX)) <0);
Метод findPicturesByPredicate
Этот метод используется для фильтрации фотографий по любому предикату.

Создание временного массива:

В начале создаётся временный массив arrayTemporary, который будет хранить фотографии, удовлетворяющие условию предиката.
java
Копировать код
Photo[] arrayTemporary = new Photo[size];
Проход по массиву фотографий:

Цикл проходит по всем фотографиям в массиве photos[], проверяя каждую фотографию с помощью метода predicate.test(photos[i]).
Если фотография удовлетворяет предикату, она добавляется во временный массив, а счётчик j увеличивается.
java
Копировать код
for (int i = 0; i < size; i++) {
    if (predicate.test(photos[i])) {
        arrayTemporary[j++] = photos[i];
    }
}
Обрезка массива:

После завершения цикла метод обрезает временный массив до фактического числа найденных фотографий с помощью Arrays.copyOf.
java
Копировать код
return Arrays.copyOf(arrayTemporary, j);
Как работают вместе методы getPhotoBetweenDate и findPicturesByPredicate:
Предикат для поиска фотографий по дате:

Метод getPhotoBetweenDate передаёт в findPicturesByPredicate предикат, который фильтрует фотографии по дате: фотографии должны находиться между dateFrom и dateTo.
Фильтрация фотографий:

В методе findPicturesByPredicate каждая фотография проверяется с помощью этого предиката. Если фотография удовлетворяет условиям (дата находится в заданном диапазоне), она добавляется в результирующий массив.
Возвращение результата:

Результатом работы метода является массив фотографий, сделанных в заданном диапазоне дат. Если ни одна фотография не удовлетворяет условию, возвращается пустой массив.
Пример работы:
Допустим, у нас есть массив фотографий, каждая из которых имеет дату. Если мы вызовем:

java
Копировать код
Photo[] result = getPhotoBetweenDate(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10));
Метод вернёт все фотографии, сделанные с 2 января 2024 года (00:00:00) по 9 января 2024 года (23:59:59).

Итог:
Предикат позволяет гибко фильтровать фотографии по любому условию, в данном случае — по дате.
getPhotoBetweenDate возвращает фотографии, сделанные между двумя датами, используя предикат для фильтрации.
Массив обрезается до фактического количества найденных фотографий, что предотвращает возврат лишних пустых элементов.
 */