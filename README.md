# imocker

## 目的

基于接口先行的原则，多个系统间依赖进行开发联调的时候，为了方便快速开发，上游系统可以先调用Mock API，待依赖系统接口Ready，再将地址切换即可。

## 举例

假如依赖的获取所有用户信息的接口名称为“/users”，方法为“GET”，系统地址为“http://api.example.com”，调用地址为“http://api.example.com/users”；
那么可以在imocker中创建接口名为“/users”的api，并设置需要的返回值。如果imocker服务的地址为“http://imocker.com”，此时Mock调用地址为“http://imocker.com/api/users”。