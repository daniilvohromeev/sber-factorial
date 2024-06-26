# Факториальный микросервис

Этот проект представляет собой микросервис, который принимает числа и вычисляет их факториал. Микросервис разработан с использованием Spring Boot и может быть запущен в Docker-контейнере.

## Функциональность

Микросервис предоставляет следующий функционал:

- Принимает на вход число в формате JSON и возвращает факториал этого числа.
- Ограничивает ввод чисел факториала до 100 для предотвращения переполнения и чрезмерного использования ресурсов.
- Возвращает технические метрики в формате Prometheus и статус сервиса через health endpoint.

## Технологии

- **Spring Boot**: Фреймворк для создания микросервисов.
- **Maven**: Сборщик проекта для управления зависимостями и сборки.
- **Prometheus**: Для мониторинга технических метрик.
- **JUnit и Mockito**: Для написания unit и интеграционных тестов.
- **Docker**: Для упаковки и развёртывания сервиса.

## Запуск проекта

### Локально

Чтобы запустить проект локально, выполните следующие шаги:

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/yourusername/factorial-service.git
2. Запустить приложение:
   ```bash
   docker compose -f docker-compose.yml up                                                          


