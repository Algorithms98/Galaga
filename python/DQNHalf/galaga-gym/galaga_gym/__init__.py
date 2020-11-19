from gym.envs.registration import register

register(
  id="GalagaEnv-v0",
  entry_point="galaga_gym.envs:GalagaEnv"
)