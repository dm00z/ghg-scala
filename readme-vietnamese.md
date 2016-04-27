# Hướng dẫn phát triển

### Dùng SourceTree để _làm việc_ với source code
Dùng SourceTree để:
+ clone / pull: **Lấy** trên github.com về máy.
+ commit: **Lưu** lại lịch sử những sửa đổi của mình (sau khi chỉnh sửa các file code)
+ push: **Đẩy** toàn bộ lịch sử sửa đổi lên github. Ví dụ: Xem lịch sử của ghg-scala [ở đây](https://github.com/giabao/ghg-scala/commits)

1. Tải & cài [SourceTree](https://www.sourcetreeapp.com/)
2. clone https://github.com/giabao/ghg-scala.git
Giả sử clone về thư mục C:\ghg-scala

### Mở & chỉnh sửa code bằng IntelliJ
_Có thể mở bằng bất kỳ chương trình gì để xem / in,.. nhưng để "phát triển" thì nên dùng IntelliJ_

1. Tải và cài [Java SE Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. Tải và cài [IntelliJ](https://www.jetbrains.com/idea/download/) - bản Community thôi cũng được.
3. Khi chạy IntelliiJ lần đầu, nó sẽ hỏi có cài plugin cho Scala không thì chọn Có cài.
4. Trong IntelliJ, chọn menu File / Open, rồi mở thư mục C:\ghg-scala

### Để "dịch" (compile) chương trình
1. Tải và cài [sbt](http://www.scala-sbt.org/0.13/docs/Setup.html)
   (Nếu dùng Windows thì có thể cài nhanh bằng [file này](https://dl.bintray.com/sbt/native-packages/sbt/0.13.11/sbt-0.13.11.msi))
2. Tải và cài [node.js](https://nodejs.org/en/download/current/)
3. Mở Command Prompt và chạy các lệnh như hướng dẫn ở file README.md
