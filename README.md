# NeoGym

**NeoGym** é uma plataforma digital voltada para o ecossistema fitness, conectando alunos, personal trainers, nutricionistas e academias em um único ambiente integrado. O objetivo é oferecer uma solução segura, confiável e estruturada, permitindo que usuários encontrem profissionais credenciados dentro da academia onde treinam, acompanhem treinos, agendem sessões e interajam via chat. Este projeto serve como **Trabalho de Conclusão de Curso (TCC)** e também como **projeto de portfólio público**, demonstrando arquitetura moderna e boas práticas de desenvolvimento.

## Objetivo do Projeto

- Conectar alunos a **profissionais credenciados**  
- Garantir que personal trainers atuem apenas nas academias onde possuem vínculo  
- Permitir acompanhamento de treinos e hidratação  
- Disponibilizar agendamento de sessões  
- Oferecer comunicação direta via chat  
- Estruturar um ambiente confiável para serviços relacionados à saúde e bem-estar  

## Tecnologias Utilizadas

- **Mobile:** Flutter (Dart)  
- **Backend:** Java + Spring Boot (API REST)  
- **Web Admin:** React  
- **Banco de Dados:** PostgreSQL  
- **Autenticação e Segurança:** JWT  
- **Comunicação em tempo real:** WebSocket (para chat e notificações)

## Perfis de Usuário

### Aluno (Mobile)
- Criar perfil e selecionar academia  
- Buscar personal trainers vinculados à sua academia  
- Registrar consumo diário de água  
- Visualizar fichas de treino  
- Agendar sessões  
- Conversar via chat com profissionais  

### Personal Trainer (Mobile)
- Cadastro com credencial profissional (CREF)  
- Upload de documentação  
- Solicitação de vínculo com academias  
- Criação de fichas de treino  
- Gerenciamento de agenda  
- Chat com alunos  

### Nutricionista (Web)
- Cadastro com credencial profissional (CRN)  
- Atendimento privado a alunos  
- Criação de planos alimentares  
- Agendamento de consultas  
- Chat com alunos  

### Academia (Web)
- Cadastro da unidade, localização e horários  
- Aprovação de personal trainers vinculados  
- Visualização de profissionais associados  

### Administrador (Web)
- Validação de credenciais profissionais  
- Aprovação ou rejeição de cadastros  
- Moderação da plataforma  
- Controle geral do sistema  

## Credenciamento e Segurança

Para garantir a confiabilidade do sistema, o NeoGym implementa validação profissional:

- Cadastro de número de registro (CREF ou CRN)  
- Upload de documentação comprobatória  
- Status de validação: ⏳ Pendente | ✅ Aprovado | ❌ Rejeitado  
- Profissionais só aparecem nas buscas após aprovação  

## Funcionalidades Principais

- Cadastro e login seguro com JWT  
- Sistema de credenciamento de profissionais  
- Busca de profissionais por academia  
- Chat em tempo real entre usuários  
- Agendamento de sessões  
- Registro de consumo diário de água  
- Criação e acompanhamento de fichas de treino  
- Gestão de academias e vínculo de profissionais  

## Arquitetura do Sistema

- Flutter para mobile (alunos e personal trainers)  
- React para web admin (nutricionistas, administradores e academias)  
- Spring Boot como backend único, controlando lógica de negócio e acesso ao banco  
- PostgreSQL para armazenamento relacional de dados  

## Regras de Negócio Importantes

- Um personal só pode atender alunos em academias onde possui vínculo ativo  
- Chat só é liberado para profissionais validados  
- Sessões sempre vinculadas a aluno, profissional e academia  
- Profissionais não aprovados não aparecem nas buscas  

## Status do Projeto

- [x] Levantamento de requisitos  
- [x] Definição de perfis e regras de negócio  
- [x] Modelagem inicial do banco de dados  
- [ ] Implementação de backend (Spring Boot)  
- [ ] Implementação do mobile (Flutter)  
- [ ] Implementação do web admin (React)  
- [ ] Funcionalidades de chat e agenda  
- [ ] Validação de credenciais e fluxo administrativo  

## Integrantes

- Guilherme de Araujo Souza  
- Diovanni Lucas Soares de Paula

## Licença

Projeto acadêmico desenvolvido para fins educacionais e portfólio público.
