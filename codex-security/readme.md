# Codex-Security

### 自定义注册
实现RegisterCustomizer接口，在调用注册接口时，会回调，可在里面进行保存操作

或者继承RegisterService，比如PasswordRegisterService，传/register?mode=password
