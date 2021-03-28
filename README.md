# 아두이노 블루투스 터미널 프로그램 입니다.(프로젝트 진행완료)

 안드로이드 앱으로 아두이노와 블루투스 통신을 지원합니다.
 - fragment를 이용하여 화면을 전개합니다. (fragment_terminal.xml)
 - Terminal(아두이노 소스제공)
 - Arduino연결 ( DevicesFragment.java, SerialXXX.java )
 - 값 전송 (send 함수 제공)
 - 값 수신 (onSerial 메소드 참고)
 - Arduino 코드소스 -> johncom.tistory.com 참고
console for Bluetooth LE (4.x) devices implementing a custom serial profile

This app includes UUIDs for widely used serial profiles:
- Nordic Semiconductor nRF51822  
- Texas Instruments CC254x
- Microchip RN4870/1
- Telit Bluemod
