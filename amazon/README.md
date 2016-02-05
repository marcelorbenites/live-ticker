Como acessar a instancia da Amazon?
===================================

IP da sua instância da Amazon: 54.84.137.13

Para acessar, use o arquivo de chave .pem deste diretorio.

$ chmod 400 selecaoGlobocomPoa.pem

$ ssh -i selecaoGlobocomPoa.pem ubuntu@54.84.137.13

O usuário ubuntu está no sudoers, é só fazer $ sudo su - que você vira root da máquina

Acessos
--------

As seguintes portas estão liberadas:
- 22 TCP
- 80 TCP
- 443 TCP
- 8080 TCP
- 8081 TCP

Se for necessário a liberação de mais alguma porta, é só pedir.
