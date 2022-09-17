from tensorflow import keras
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D, BatchNormalization
from keras.layers import GlobalAveragePooling2D
from tensorflow.compat.v1 import ConfigProto
from tensorflow.compat.v1 import InteractiveSession

config = ConfigProto()
config.gpu_options.allow_growth = True
session = InteractiveSession(config=config)

import gzip
import pickle
f = gzip.open('C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/src/main/resources/fileProp/mnist.pkl.gz', 'rb')

data = pickle.load(f, encoding='bytes')

f.close()
(x_train, y_train), (x_test, y_test) = data

epochs = 10
input_shape = (x_train.shape[1],
               x_train.shape[2],
               1 if len(x_train.shape) == 3 else x_train.shape[3])
x_train = x_train.astype('float32')
x_test = x_test.astype('float32')
x_train /= 255
x_test /= 255
img_rows, img_cols = x_train.shape[1], x_train.shape[2]

batch_size = 128

num_classes = 10
y_train = keras.utils.to_categorical(y_train, num_classes)
y_test = keras.utils.to_categorical(y_test, num_classes)

model = Sequential()
model.add(Conv2D(padding="valid", input_shape=input_shape, filters=128, activation="relu", kernel_size=(6,6)))
model.add(Conv2D(padding="valid", filters=128, activation="relu", kernel_size=(6,6)))
model.add(MaxPooling2D(padding="valid", pool_size=(2,2)))
model.add(Dropout(rate=0.5))
model.add(Flatten())
model.add(Dense(units=128, activation="relu"))
model.add(Dense(units=256, activation="relu"))
model.add(Dropout(rate=0.75))
model.add(Dense(units=10, activation="softmax"))
model.summary()
optimizer = keras.optimizers.Adam(epsilon=1e-08, beta_1=0.9, beta_2=0.999, learning_rate=0.001)
model.compile(loss='categorical_crossentropy', 
              optimizer=optimizer, 
              metrics=['accuracy'])

model.save('C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/model.h5')

history = model.fit(x_train, y_train,
                    batch_size=batch_size,
                    epochs=epochs,
                    verbose=2,
                    validation_data=(x_test, y_test))

score = model.evaluate(x_test, y_test, verbose=0)

print('Test loss:', score[0])
print('Test accuracy:', score[1])

import matplotlib.pyplot as plt
plt.plot(history.history['accuracy'])
plt.plot(history.history['val_accuracy'])
plt.title('Model accuracy')
plt.ylabel('Accuracy')
plt.xlabel('Epoch')
plt.legend(['Train', 'Test'], loc='upper left')
plt.savefig('C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/accuracy.jpg')

plt.clf()
plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('Model loss')
plt.ylabel('Loss')
plt.xlabel('Epoch')
plt.legend(['Train', 'Test'], loc='upper left')
plt.savefig('C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/loss.jpg')


